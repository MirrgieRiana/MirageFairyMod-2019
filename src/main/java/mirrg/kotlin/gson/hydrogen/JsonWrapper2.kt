@file:Suppress("MemberVisibilityCanBePrivate", "unused")

package mirrg.kotlin.gson.hydrogen

import com.google.gson.GsonBuilder
import com.google.gson.JsonArray
import com.google.gson.JsonElement
import com.google.gson.JsonNull
import com.google.gson.JsonObject
import com.google.gson.JsonPrimitive
import mirrg.kotlin.hydrogen.castOrNull

// 本体

class JsonDecompositionException(message: String) : IllegalStateException(message)

/**
 * @param jsonElement nullの場合、存在しない要素を表します。
 * Json内でnullが明示された場合はJsonNullの状態を持ちます。
 */
class JsonWrapper2(
    val jsonElement: JsonElement?,
    val path: String
) {
    override fun toString(): String = jsonElement.toString()

    // 型チェック
    // 厳密に一致する場合のみtrue、かつjsonElementが対応する子クラスであることを保証
    val isUndefined get() = jsonElement == null
    val isNumber get() = jsonElement?.castOrNull<JsonPrimitive>()?.isNumber ?: false
    val isString get() = jsonElement?.castOrNull<JsonPrimitive>()?.isString ?: false
    val isBoolean get() = jsonElement?.castOrNull<JsonPrimitive>()?.isBoolean ?: false
    val isNull get() = jsonElement?.castOrNull<JsonNull>()?.let { true } ?: false
    val isArray get() = jsonElement?.castOrNull<JsonArray>()?.let { true } ?: false
    val isObject get() = jsonElement?.castOrNull<JsonObject>()?.let { true } ?: false

    // キャスト
    // キャストできない場合は特殊化例外
    fun asJsonPrimitive() = jsonElement as? JsonPrimitive ?: e("JsonPrimitive")
    fun asJsonNull() = jsonElement as? JsonNull ?: e("JsonNull")
    fun asJsonArray() = jsonElement as? JsonArray ?: e("JsonArray")
    fun asJsonObject() = jsonElement as? JsonObject ?: e("JsonObject")

    // 取得プロパティ
    // キャストできない場合に発生する例外は必ず特殊化クラス
    // 省略時も例外を出す
    fun asUndefined() = if (isUndefined) null else e("Undefined")
    fun asInt() = if (isNumber) asJsonPrimitive().asInt else e("Number")
    fun asLong() = if (isNumber) asJsonPrimitive().asLong else e("Number")
    fun asDouble() = if (isNumber) asJsonPrimitive().asDouble else e("Number")
    fun asBigDecimal() = if (isNumber) asJsonPrimitive().asBigDecimal!! else e("Number")
    fun asString() = if (isString) asJsonPrimitive().asString!! else e("String")
    fun asBoolean() = if (isBoolean) asJsonPrimitive().asBoolean else e("Boolean")
    fun asNull() = if (isNull) null else e("Null")
    fun asList(): List<JsonWrapper2> = if (isArray) asJsonArray().toList().mapIndexed { index, item -> JsonWrapper2(item, "$path[$index]") } else e("Array")
    fun asMap(): Map<String, JsonWrapper2> = if (isObject) asJsonObject().entrySet().associate { (key, value) -> key to JsonWrapper2(value, "$path.$key") } else e("Object")

    // 「undefinedもしくはnullのときに」nullを返す
    val orNull get() = if (isUndefined || isNull) null else this

    // オブジェクトと配列は子要素をJsonWrapperで覆って返す
    operator fun get(index: Int) = JsonWrapper2(if (index >= 0 && index < asJsonArray().size()) asJsonArray().get(index) else null, "$path[$index]")
    operator fun get(key: String) = JsonWrapper2(asJsonObject().get(key), "$path.$key")
}

private val JsonWrapper2.type
    get() = when {
        isUndefined -> "Undefined"
        isNumber -> "Number"
        isString -> "String"
        isBoolean -> "Boolean"
        isNull -> "Null"
        isArray -> "Array"
        isObject -> "Object"
        else -> throw IllegalStateException()
    }

private fun JsonWrapper2.e(expectedType: String): Nothing = throw JsonDecompositionException("Expected $expectedType, but is a $type: $path")

// 文字列のJsonWrapper化
val String.jsonWrapper2 get() = (if (this.isBlank()) null else GsonBuilder().create().fromJson(this, JsonElement::class.java)).jsonWrapper2
val JsonElement?.jsonWrapper2 get() = JsonWrapper2(this, "_")
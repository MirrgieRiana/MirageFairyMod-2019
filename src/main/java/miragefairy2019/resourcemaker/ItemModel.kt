package miragefairy2019.resourcemaker

import com.google.gson.JsonElement
import miragefairy2019.libkt.ItemInitializer
import miragefairy2019.libkt.ItemMulti
import miragefairy2019.libkt.ItemVariant
import miragefairy2019.libkt.ItemVariantInitializer
import miragefairy2019.libkt.ModInitializer
import miragefairy2019.libkt.ResourceName
import miragefairy2019.libkt.map
import mirrg.kotlin.gson.hydrogen.jsonElement
import mirrg.kotlin.gson.hydrogen.jsonObject
import mirrg.kotlin.gson.hydrogen.jsonObjectNotNull
import net.minecraft.item.Item


fun ModInitializer.makeItemModel(path: String, creator: MakeItemModelScope2.() -> JsonElement) = onMakeResource {
    place("assets/$modId/models/item/${path}.json", MakeItemModelScope2(ResourceName(modId, path)).creator())
}

class MakeItemModelScope2(val resourceName: ResourceName)


val MakeItemModelScope2.generated get() = ItemModel.generated(resourceName.map { "items/$it" })
val MakeItemModelScope2.handheld get() = ItemModel.handheld(resourceName.map { "items/$it" })
val MakeItemModelScope2.block get() = ItemModel.block(resourceName.map { "block/$it" })
val MakeItemModelScope2.fluid get() = ItemModel.generated(resourceName.map { "blocks/${it}_still" })

object ItemModel

fun ItemModel.generated(resourceName: ResourceName) = jsonObject(
    "parent" to "item/generated".jsonElement,
    "textures" to jsonObject(
        "layer0" to "$resourceName".jsonElement
    )
)

fun ItemModel.handheld(resourceName: ResourceName) = jsonObject(
    "parent" to "item/handheld".jsonElement,
    "textures" to jsonObject(
        "layer0" to "$resourceName".jsonElement
    )
)

fun ItemModel.block(parent: ResourceName) = jsonObject(
    "parent" to "$parent".jsonElement
)

@Deprecated("Deleting")
fun <I : Item> ItemInitializer<I>.makeItemModel(creator: MakeItemModelScope<I>.() -> DataItemModel) = modInitializer.onMakeResource {
    place("assets/${registryName.domain}/models/item/${registryName.path}.json", MakeItemModelScope(this@makeItemModel).creator().jsonElement)
}

class MakeItemModelScope<I : Item>(val itemInitializer: ItemInitializer<I>)

@Deprecated("Deleting")
fun <I : ItemMulti<V>, V : ItemVariant> ItemVariantInitializer<I, V>.makeItemVariantModel(creator: MakeItemVariantModelScope<I, V>.() -> DataItemModel) = itemInitializer.modInitializer.onMakeResource {
    place("assets/${registryName.domain}/models/item/${registryName.path}.json", MakeItemVariantModelScope(this@makeItemVariantModel).creator().jsonElement)
}

class MakeItemVariantModelScope<I : ItemMulti<V>, V : ItemVariant>(val itemVariantInitializer: ItemVariantInitializer<I, V>)

data class DataItemModel(
    val parent: String,
    val elements: JsonElement? = null,
    val textures: Map<String, String>? = null
) {
    val jsonElement = jsonObjectNotNull(
        "parent" to parent.jsonElement,
        "elements" to elements,
        "textures" to textures?.mapValues { (_, it) -> it.jsonElement }?.jsonObject
    )
}

fun <I : Item> MakeItemModelScope<I>.getStandardItemModel(parent: String) = DataItemModel(
    parent = parent,
    textures = mapOf("layer0" to "${itemInitializer.registryName.domain}:items/${itemInitializer.registryName.path}")
)

fun <I : ItemMulti<V>, V : ItemVariant> MakeItemVariantModelScope<I, V>.getStandardItemModel(parent: String) = DataItemModel(
    parent = parent,
    textures = mapOf("layer0" to "${itemVariantInitializer.registryName.domain}:items/${itemVariantInitializer.registryName.path}")
)

val <I : Item> MakeItemModelScope<I>.generated get() = getStandardItemModel("item/generated")
val <I : ItemMulti<V>, V : ItemVariant> MakeItemVariantModelScope<I, V>.generated get() = getStandardItemModel("item/generated")
val <I : Item> MakeItemModelScope<I>.handheld get() = getStandardItemModel("item/handheld")
val <I : ItemMulti<V>, V : ItemVariant> MakeItemVariantModelScope<I, V>.handheld get() = getStandardItemModel("item/handheld")
val <I : Item> MakeItemModelScope<I>.block get() = DataItemModel(parent = "${itemInitializer.registryName.domain}:block/${itemInitializer.registryName.path}")
val <I : ItemMulti<V>, V : ItemVariant> MakeItemVariantModelScope<I, V>.block get() = DataItemModel(parent = "${itemVariantInitializer.registryName.domain}:block/${itemVariantInitializer.registryName.path}")

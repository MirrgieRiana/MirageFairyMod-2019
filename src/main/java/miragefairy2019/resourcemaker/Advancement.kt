package miragefairy2019.resourcemaker

import com.google.gson.JsonElement
import miragefairy2019.libkt.ModInitializer
import miragefairy2019.libkt.NamedInitializer

fun ModInitializer.makeAdvancement(path: String, creator: () -> JsonElement) = onMakeResource {
    place("assets/$modId/advancements/$path.json", creator())
}

fun NamedInitializer.makeAdvancement(creator: () -> JsonElement) = modInitializer.onMakeResource {
    place("assets/${resourceName.domain}/advancements/${resourceName.path}.json", creator())
}

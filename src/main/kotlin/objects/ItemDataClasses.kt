package cz.lukynka.objects

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
data class Item(
    val parent: String,
    val textures: ItemTextures,
    val overrides: MutableList<ItemOverride>? = null
) {
    fun toJson(): String {
        return Json.encodeToString<Item>(this)
    }
}

@Serializable
data class ItemOverride(
    val predicate: Predicate,
    val model: String
)

@Serializable
data class Predicate(
    @SerialName("custom_model_data")
    val customModelData: Int
)

@Serializable
data class ItemTextures(
    @SerialName("layer0")
    val layer: String
)
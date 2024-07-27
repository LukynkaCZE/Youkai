package cz.lukynka

import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

fun atlasFileContent(name: String): ResourceAtlas {
    return ResourceAtlas(AtlasSource("directory", name, "$name/"))
}

@Serializable
data class AtlasSource(
    val type: String,
    val source: String,
    val prefix: String
) {
}

@Serializable
data class ResourceAtlas(
    val sources: List<AtlasSource>
) {
    constructor(vararg source: AtlasSource): this(source.toList())

    fun toJson(): String {
        return Json.encodeToString<ResourceAtlas>(this)
    }
}
package cz.lukynka.resourcepack

import kotlinx.serialization.*
import kotlinx.serialization.json.Json

@Serializable
data class PackFormat(
    @SerialName("pack_format")
    var version: PackVersion,
    var description: String,
) {

}
//TODO Language and Filter
@Serializable
data class PackMcMeta(
    val pack: PackFormat
) {
    fun toJson(): String {
        var text = Json.encodeToString<PackMcMeta>(this)
        PackVersion.entries.forEach {
            text = text.replace("\"${it.name}\"", it.version.toString())
        }
        return text
    }
}


@Serializable
enum class PackVersion(var version: Int) {
    MC_1_21(34);
}
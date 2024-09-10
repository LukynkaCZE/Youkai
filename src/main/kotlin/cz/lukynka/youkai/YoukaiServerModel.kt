package cz.lukynka.youkai

import kotlinx.serialization.Serializable

@Serializable
data class YoukaiServerModel(
    val modelId: String,
    var customModelId: Int? = null,
    val baseMaterial: String
)
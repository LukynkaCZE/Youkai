package cz.lukynka.api

import kotlinx.serialization.Serializable

@Serializable
data class YoukaiServerModel(
    val modelId: String,
    val customModelId: Int,
    val baseMaterial: String
)

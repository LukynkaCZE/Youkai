package cz.lukynka.youkai.minecraft

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class MinecraftModel(
    val credit: String? = null,
    val ambientocclusion: Boolean? = null,
    @SerialName("texture_size")
    val textureSize: MutableList<Double>? = null,
    val textures: MutableMap<String, String>? = null,
    val elements: MutableList<MinecraftModelElement>? = null,
    val display: Display? = null,
)

@Serializable
data class MinecraftModelElement(
    val from: MutableList<Double>,
    val to: MutableList<Double>,
    val rotation: MinecraftModelRotation? = null,
    val faces: MinecraftModelFaces? = null,
    val name: String? = null,
)

@Serializable
data class MinecraftModelRotation(
    val angle: Double? = null,
    val axis: String? = null,
    val origin: MutableList<Double>? = null,
)

@Serializable
data class MinecraftModelFaces(
    val north: North? = null,
    val east: East? = null,
    val south: South? = null,
    val west: West? = null,
    val up: Up? = null,
    val down: Down? = null,
)

@Serializable
data class North(
    val uv: MutableList<Double>? = null,
    val texture: String? = null,
    val rotation: Double? = null
)

@Serializable
data class East(
    val uv: MutableList<Double>? = null,
    val texture: String? = null,
    val rotation: Double? = null
)

@Serializable
data class South(
    val uv: MutableList<Double>? = null,
    val texture: String? = null,
    val rotation: Double? = null
)

@Serializable
data class West(
    val uv: MutableList<Double>? = null,
    val texture: String? = null,
    val rotation: Double? = null
)

@Serializable
data class Up(
    val uv: MutableList<Double>? = null,
    val texture: String? = null,
    val rotation: Double? = null
)

@Serializable
data class Down(
    val uv: MutableList<Double>? = null,
    val texture: String? = null,
    val rotation: Double? = null
)

@Serializable
data class Display(
    @SerialName("thirdperson_righthand")
    val thirdpersonRighthand: ThirdpersonRighthand? = null,
    @SerialName("firstperson_righthand")
    val firstpersonRighthand: FirstpersonRighthand? = null,
    val ground: Ground? = null,
    val gui: Gui? = null,
    val head: Head? = null,
    val fixed: Fixed? = null,
)

@Serializable
data class ThirdpersonRighthand(
    val rotation: MutableList<Double>? = null,
    val translation: MutableList<Double>? = null,
    val scale: MutableList<Double>? = null,
)

@Serializable
data class FirstpersonRighthand(
    val translation: MutableList<Double>? = null,
    val scale: MutableList<Double>? = null,
)

@Serializable
data class Ground(
    val translation: MutableList<Double>? = null,
    val scale: MutableList<Double>? = null,
)

@Serializable
data class Gui(
    val rotation: MutableList<Double>? = null,
    val translation: MutableList<Double>? = null,
    val scale: MutableList<Double>? = null,
)

@Serializable
data class Head(
    val translation: MutableList<Double>? = null,
    val scale: MutableList<Double>? = null,
)

@Serializable
data class Fixed(
    val rotation: MutableList<Double>? = null,
    val translation: MutableList<Double>? = null,
    val scale: MutableList<Double>? = null,
)

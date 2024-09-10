package cz.lukynka.youkai.objects

import cz.lukynka.youkai.config.ConfigManager
import cz.lukynka.youkai.obfuscation.Obfuscatory
import java.io.File

class Custom3dModel(val modelFile: File, val textureFile: File): ResourcepackObject {

    init {
        verify()
    }

    val config = ConfigManager.currentConfig.compiler

    override fun verify() {
        if(!modelFile.exists()) throw Exception("File ${modelFile.name} does not exist!")
        if(!textureFile.exists()) throw Exception("File ${modelFile.name} does not exist!")
        if(modelFile.extension != "json") throw Exception("File ${modelFile.name} is not in the JSON format!")
        if(textureFile.extension != "png") throw Exception("File ${modelFile.name} is not in the PNG format!")
    }

    val modelObf = Obfuscatory.getNext()
    val textureObf = Obfuscatory.getNext()

    fun getTextureAssetName(): String {
        return if(config.obfuscation) "${textureObf}.${textureFile.extension}" else textureFile.name
    }

    fun getTextureAssetNameWithoutExtension(): String {
        return if(config.obfuscation) textureObf else textureFile.nameWithoutExtension
    }

    fun getModelAssetName(): String {
        return if(config.obfuscation) "${modelObf}.${modelFile.extension}" else modelFile.name
    }

    fun getModelAssetNameWithoutExtension(): String {
        return if(config.obfuscation) modelObf else modelFile.nameWithoutExtension
    }
}
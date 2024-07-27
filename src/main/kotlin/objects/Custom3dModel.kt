package cz.lukynka.objects

import cz.lukynka.Config
import cz.lukynka.obfuscation.Obfuscatory
import java.io.File

class Custom3dModel(val modelFile: File, val textureFile: File): ResourcepackObject {

    init {
        verify()
    }

    override fun verify() {
        if(!modelFile.exists()) throw Exception("File ${modelFile.name} does not exist!")
        if(!textureFile.exists()) throw Exception("File ${modelFile.name} does not exist!")
        if(modelFile.extension != "json") throw Exception("File ${modelFile.name} is not in the JSON format!")
        if(textureFile.extension != "png") throw Exception("File ${modelFile.name} is not in the PNG format!")
    }

    val modelObf = Obfuscatory.getNext()
    val textureObf = Obfuscatory.getNext()

    fun getTextureAssetName(): String {
        return if(Config.OBFUSCATE) "${textureObf}.${textureFile.extension}" else textureFile.name
    }

    fun getTextureAssetNameWithoutExtension(): String {
        return if(Config.OBFUSCATE) textureObf else textureFile.nameWithoutExtension
    }

    fun getModelAssetName(): String {
        return if(Config.OBFUSCATE) "${modelObf}.${modelFile.extension}" else modelFile.name
    }

    fun getModelAssetNameWithoutExtension(): String {
        return if(Config.OBFUSCATE) modelObf else modelFile.nameWithoutExtension
    }
}
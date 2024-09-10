package cz.lukynka.youkai.objects

import cz.lukynka.youkai.Config
import cz.lukynka.youkai.obfuscation.Obfuscatory
import java.io.File

class Custom2dItem(val file: File): ResourcepackObject {

    val obf = Obfuscatory.getNext()

    init {
        verify()
    }

    override fun verify() {
        if(!file.exists()) throw Exception("File ${file.name} does not exist!")
        if(file.extension != "png") throw Exception("File ${file.name} is not in the PNG format!")
    }

    fun getAssetName(): String {
        return if(Config.OBFUSCATE) "${obf}.${file.extension}" else file.name
    }

    fun getAssetNameWithoutExtension(): String {
        return if(Config.OBFUSCATE) obf else file.nameWithoutExtension
    }

    fun getNonObfAssetNameWithoutExtension(): String {
        return file.nameWithoutExtension
    }
}
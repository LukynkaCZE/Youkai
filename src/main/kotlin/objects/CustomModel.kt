package cz.lukynka.objects

import java.io.File

class CustomModel(val modelFile: File, val textureFile: File): ResourcepackObject {

    init {
        verify()
    }

    override fun verify() {
        if(!modelFile.exists()) throw Exception("File ${modelFile.name} does not exist!")
        if(!textureFile.exists()) throw Exception("File ${modelFile.name} does not exist!")
        if(modelFile.extension == "json") throw Exception("File ${modelFile.name} is not in the JSON format!")
        if(textureFile.extension == "png") throw Exception("File ${modelFile.name} is not in the PNG format!")
    }
}
package cz.lukynka.objects

import cz.lukynka.prettylog.log
import java.io.File

class Custom2dItem(val file: File): ResourcepackObject {

    init {
        verify()
    }

    override fun verify() {
        if(!file.exists()) throw Exception("File ${file.name} does not exist!")
        if(file.extension != "png") throw Exception("File ${file.name} is not in the PNG format!")
    }
}
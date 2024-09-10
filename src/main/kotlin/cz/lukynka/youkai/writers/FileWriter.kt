package cz.lukynka.youkai.writers

import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

fun File.writeTextAndClose(string: String) {
    val output = FileOutputStream(this)
    output.write(string.toByteArray())
    output.close()
}

fun File.readTextAndClose(string: String): String {
    val input = FileInputStream(this)
    val bytes = input.readAllBytes()
    val output = bytes.toString()
    input.close()
    return output
}
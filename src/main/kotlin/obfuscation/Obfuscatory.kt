package cz.lukynka.obfuscation

import java.util.*

object Obfuscatory {

    fun getNext(): String {
        return UUID.randomUUID().toString()
    }
}
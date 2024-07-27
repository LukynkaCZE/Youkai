package cz.lukynka.obfuscation

import cz.lukynka.Config
import java.util.*

object Obfuscatory {

    private val alphabet = ('a'..'z').toList()
    private var currentString = ""
    private var currentIndex = 0

    init {
        when(Config.OBFUSCATION_STRATEGY) {
            ObfuscationStrategy.UUID -> {}
            ObfuscationStrategy.ALPHABET -> currentString = alphabet[0].toString()
            ObfuscationStrategy.BASE64 -> {}
            ObfuscationStrategy.UWU_OWO_MEOW -> currentString = "uwu"
        }
    }

    fun getNext(): String {
        when(Config.OBFUSCATION_STRATEGY) {
            ObfuscationStrategy.UUID -> return UUID.randomUUID().toString()
            ObfuscationStrategy.ALPHABET -> {
                val nextString = currentString

                currentIndex++
                if (currentIndex == alphabet.size) {
                    currentIndex = 0
                    currentString += alphabet[0]
                } else {
                    currentString = currentString.dropLast(1) + alphabet[currentIndex]
                }

                return nextString
            }
            ObfuscationStrategy.BASE64 -> {
                val randomBytes = ByteArray(32)
                Random().nextBytes(randomBytes)

                return Base64.getUrlEncoder()
                    .encodeToString(randomBytes)
                    .replace("=", Random().nextInt(0, 9).toString())
                    .replace("-", Random().nextInt(0, 9).toString())
                    .lowercase()
            }
            ObfuscationStrategy.UWU_OWO_MEOW -> {
                val wordSet = listOf("uwu", "owo", "meow")

                val nextString = currentString + wordSet[currentIndex]
                currentIndex = (currentIndex + 1) % wordSet.size

                currentString = nextString
                return nextString
            }
        }
    }
}

enum class ObfuscationStrategy {
    UUID,
    ALPHABET,
    BASE64,
    UWU_OWO_MEOW
}
package cz.lukynka.youkai.obfuscation

import cz.lukynka.youkai.config.ConfigManager
import kotlinx.serialization.Serializable
import java.util.*

object Obfuscatory {

    private val alphabet = ('a'..'z').toList()
    private var currentString = ""
    private var currentIndex = 0

    val config = ConfigManager.currentConfig.compiler

    init {
        when(config.obfuscationStrategy) {
            ObfuscationStrategy.UUID -> {}
            ObfuscationStrategy.ALPHABET -> currentString = alphabet[0].toString()
            ObfuscationStrategy.BASE64 -> {}
            ObfuscationStrategy.UWU_OWO_MEOW -> currentString = "uwu"
        }
    }

    fun getNext(): String {
        when(config.obfuscationStrategy) {
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

@Serializable
enum class ObfuscationStrategy {
    UUID,
    ALPHABET,
    BASE64,
    UWU_OWO_MEOW
}
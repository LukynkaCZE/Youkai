package cz.lukynka

import cz.lukynka.obfuscation.ObfuscationStrategy

object Config {
    const val AUTH_ENABLED = true
    const val PORT = 6969
    const val ITEM = "iron_ingot"
    const val OBFUSCATE = false
    const val SHUFFLE_IDS_ON_COMPILE = true
    val OBFUSCATION_STRATEGY = ObfuscationStrategy.BASE64
}
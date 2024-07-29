package cz.lukynka

import cz.lukynka.obfuscation.ObfuscationStrategy

object Config {
    const val AUTH_ENABLED = true
    val PACK_DATA_REQUESTS = listOf(
        "http://localhost:6969/youkai-sync"
    )
    val TOKEN = "qDSIerBkcfXCudkjDd296esWfAEBFB7sZtyKaBaHvqta2RfTP5b3S5GuddbRbofC"
    const val ITEM = "raw_copper"
    const val OBFUSCATE = true
    const val SHUFFLE_IDS_ON_COMPILE = true
    const val COMPILE_TO_ZIP = false
    val OBFUSCATION_STRATEGY = ObfuscationStrategy.BASE64
}

enum class YoukaiType {
    SERVER_PLUGIN,
    COMPILER
}
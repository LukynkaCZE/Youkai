package cz.lukynka.compilers

import cz.lukynka.Config
import cz.lukynka.obfuscation.Obfuscatory
import cz.lukynka.prettylog.AnsiColor
import cz.lukynka.prettylog.LogType
import cz.lukynka.prettylog.log
import cz.lukynka.resourcepack.YoukaiPack

class BasePackCompiler(val pack: YoukaiPack, val path: String) {

    val youkaiCompiledTextures = if(Config.OBFUSCATE) Obfuscatory.getNext() else "youkai-compiled"
    val youkaiCompiledModels = if(Config.OBFUSCATE) Obfuscatory.getNext() else "youkai-compiled"

    val name = if(Config.OBFUSCATE) Obfuscatory.getNext() else pack.name

    val compilers = listOf<Compiler>(
        PackFormatCompiler(this),
        AtlasCompiler(this),
        CustomModelsCompiler(this),
        ZipCompiler(this)
    )

    fun compile() {
        val results = mutableListOf<CompiledResult>()
        var success = 0
        var failed = 0
        var total = 0
        compilers.forEach {
            try {
                val result = it.compile()
                results.add(result)
                success++
                total++
                log("[${total}/${compilers.size}] ${it::class.simpleName} finished!", LogType.SUCCESS)
            } catch (ex: Exception) {
                failed++
                total++
                log("[${total}/${compilers.size}] Error when compiling ${it::class.simpleName}: $ex", LogType.ERROR)
                log(ex)
            }
        }
        log("[${total}/${compilers.size}] Finished compiling pack ${pack.name}!", LogType.SUCCESS)
        log("- ${AnsiColor.AQUA}$total total", LogType.INFORMATION)
        log("- ${AnsiColor.BRIGHT_GREEN}$success successful", LogType.INFORMATION)
        log("- ${AnsiColor.RED}$failed failed", LogType.INFORMATION)
    }
}
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

    private val compilers = listOf<Compiler>(
        PackFormatCompiler(this),
        AtlasCompiler(this),
        CustomModelsCompiler(this),
        ZipCompiler(this)
    )

    fun compile() {
        val results = compilers.mapIndexed { index, compiler ->
            try {
                val result = compiler.compile()
                log("[${index + 1}/${compilers.size}] ${compiler::class.simpleName} finished!", LogType.SUCCESS)
                result
            } catch (ex: Exception) {
                log("[${index + 1}/${compilers.size}] Error when compiling ${compiler::class.simpleName}: $ex", LogType.ERROR)
                log(ex)
                null
            }
        }

        val successful = results.count { it != null }
        val failed = results.size - successful

        log("[${compilers.size}/${compilers.size}] Finished compiling pack ${pack.name}!", LogType.SUCCESS)
        log("- ${AnsiColor.AQUA}${results.size} total", LogType.INFORMATION)
        log("- ${AnsiColor.BRIGHT_GREEN}$successful successful", LogType.INFORMATION)
        log("- ${AnsiColor.RED}$failed failed", LogType.INFORMATION)
    }
}
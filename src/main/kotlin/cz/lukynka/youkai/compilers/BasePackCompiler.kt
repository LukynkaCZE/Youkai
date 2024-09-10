package cz.lukynka.youkai.compilers

import cz.lukynka.youkai.compilers.*
import cz.lukynka.youkai.Config
import cz.lukynka.youkai.YoukaiServerModel
import cz.lukynka.youkai.YoukaiSync
import cz.lukynka.youkai.sendCompiledPackRequest
import cz.lukynka.youkai.obfuscation.Obfuscatory
import cz.lukynka.prettylog.AnsiColor
import cz.lukynka.prettylog.LogType
import cz.lukynka.prettylog.log
import cz.lukynka.youkai.resourcepack.YoukaiPack
import java.io.File
import java.util.UUID

class BasePackCompiler(val pack: YoukaiPack, val outputPath: String) {

    val path = "temp/${UUID.randomUUID()}"

    fun cleanup() {
        File(path).deleteRecursively()
    }

    val youkaiCompiledTextures = if(Config.OBFUSCATE) Obfuscatory.getNext() else "youkai-compiled"
    val youkaiCompiledModels = if(Config.OBFUSCATE) Obfuscatory.getNext() else "youkai-compiled"

    val name = if(Config.OBFUSCATE) Obfuscatory.getNext() else pack.name

    private val compilers = mutableListOf<Compiler>(
        PrepareFilesCompiler(this),
        MergeCompiler(this),
        PackFormatCompiler(this),
        AtlasCompiler(this),
        CustomModelsCompiler(this),
        ZipCompiler(this)
    )

    fun compile() {
        if(Config.COMPILE_TO_ZIP) compilers.add(ZipCompiler(this))

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

        log("$outputPath/${pack.compiledPackFileName}.zip")
        File("temp/${pack.compiledPackFileName}.zip").copyRecursively(File("$outputPath/${pack.compiledPackFileName}.zip"), true)

        val models = mutableListOf<YoukaiServerModel>()
        results.forEach { result ->
            when(result) {
                is CustomModelResponse -> {
                    result.items.forEach { resultItem ->
                        models.add(resultItem)
                    }
                }
            }
        }

        val sync = YoukaiSync(models)
        sendCompiledPackRequest(sync)
        cleanup()
    }
}
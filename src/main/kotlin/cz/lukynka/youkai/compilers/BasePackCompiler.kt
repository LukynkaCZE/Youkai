package cz.lukynka.youkai.compilers

import cz.lukynka.youkai.YoukaiServerModel
import cz.lukynka.youkai.YoukaiSync
import cz.lukynka.youkai.sendCompiledPackRequest
import cz.lukynka.youkai.obfuscation.Obfuscatory
import cz.lukynka.prettylog.AnsiColor
import cz.lukynka.prettylog.LogType
import cz.lukynka.prettylog.log
import cz.lukynka.youkai.config.ConfigManager
import cz.lukynka.youkai.config.PublishingApis.*
import cz.lukynka.youkai.publishing.AmazonS3Publisher
import cz.lukynka.youkai.resourcepack.YoukaiPack
import cz.lukynka.youkai.publishing.CloudflareR2Publisher
import java.io.File
import java.util.UUID

class BasePackCompiler(val pack: YoukaiPack, val outputPath: String) {

    val path = "temp/${UUID.randomUUID()}"

    fun cleanup() {
        File(path).deleteRecursively()
    }

    val config = ConfigManager.currentConfig.compiler

    val youkaiCompiledTextures = if(config.obfuscation) Obfuscatory.getNext() else "youkai-compiled"
    val youkaiCompiledModels = if(config.obfuscation) Obfuscatory.getNext() else "youkai-compiled"

    val name = if(config.obfuscation) Obfuscatory.getNext() else pack.name

    private val compilers = mutableListOf<Compiler>(
        PrepareFilesCompiler(this),
        MergeCompiler(this),
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

        val zipFile = File("temp/${pack.compiledPackFileName}.zip")
        val outputFile = File("$outputPath/${pack.compiledPackFileName}.zip")

        outputFile.deleteRecursively()
        outputFile.delete()
        zipFile.copyRecursively(outputFile, true)

        val publishing = ConfigManager.currentConfig.publishing
        if(publishing.publish) {
            val type = publishing.publishingService
            val publisher = when(type) {
                CLOUDFLARE_R2 -> CloudflareR2Publisher()
                AMAZON_S3 -> AmazonS3Publisher()
            }
            publisher.push(zipFile)
        }

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
package cz.lukynka.youkai.compilers

import cz.lukynka.youkai.writers.writeTextAndClose
import kotlinx.serialization.Serializable
import java.io.File

class PackFormatCompiler(var compiler: BasePackCompiler): Compiler {

    override fun compile(): CompiledResult {
        val packFormat = compiler.pack.packFormat
        val packMcMeta = File("${compiler.path}/pack.mcmeta")
        packMcMeta.writeTextAndClose(packFormat.toJson())
        return PackFormatResult(packFormat.pack.version.version, packFormat.pack.description)
    }
}

@Serializable
data class PackFormatResult(
    val version: Int,
    val description: String
): CompiledResult
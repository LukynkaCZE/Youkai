package cz.lukynka.compilers

import kotlinx.serialization.Serializable
import java.io.File

class PackFormatCompiler(var compiler: BasePackCompiler): Compiler {

    override fun compile(): CompiledResult {
        val packFormat = compiler.pack.packFormat
        val packMcMeta = File("${compiler.path}/pack.mcmeta")
        packMcMeta.writeText(packFormat.toJson())
        return PackFormatResult(packFormat.pack.version.version, packFormat.pack.description)
    }
}

@Serializable
data class PackFormatResult(
    val version: Int,
    val description: String
): CompiledResult
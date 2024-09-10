package cz.lukynka.youkai.compilers

import kotlinx.serialization.Serializable
import net.lingala.zip4j.ZipFile
import java.io.*


class ZipCompiler(val compiler: BasePackCompiler): Compiler {

    override fun compile(): CompiledResult {

        val zipFile = ZipFile(File(compiler.path).parentFile.path + "/${compiler.pack.compiledPackFileName}.zip")
        val directory = File(compiler.path)
        directory.walkTopDown().iterator().forEach {
            if(it.isDirectory) {
                zipFile.addFolder(it)
            } else {
                zipFile.addFile(it)
            }
        }
        return ZipCompilerResult(zipFile.bufferSize.toLong())
    }
}

@Serializable
data class ZipCompilerResult(
    val packSize: Long,
): CompiledResult

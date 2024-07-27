package cz.lukynka.compilers

import cz.lukynka.prettylog.log
import kotlinx.serialization.Serializable
import java.io.*
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

class ZipCompiler(val compiler: BasePackCompiler): Compiler {

    override fun compile(): CompiledResult {
        val zipFile = File(File(compiler.path).parentFile.path + "/${compiler.pack.compiledPackFileName}.zip")
        val directory = File(compiler.path)
        ZipOutputStream(zipFile.outputStream()).use { zos ->
            directory.walkTopDown().forEach { file ->
                val relativePath = file.relativeTo(directory).path
                if (file.isDirectory) {
                    zos.putNextEntry(ZipEntry("$relativePath/"))
                    zos.closeEntry()
                } else {
                    zos.putNextEntry(ZipEntry(relativePath))
                    file.inputStream().copyTo(zos)
                    zos.closeEntry()
                }
            }
        }
        return ZipCompilerResult(zipFile.length())
    }
}

@Serializable
data class ZipCompilerResult(
    val packSize: Long
): CompiledResult
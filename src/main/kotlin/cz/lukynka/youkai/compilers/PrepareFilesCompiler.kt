package cz.lukynka.youkai.compilers

import java.io.File

class PrepareFilesCompiler(private var compiler: BasePackCompiler): Compiler {

    override fun compile(): CompiledResult {
        val out = File(compiler.path)
        out.deleteRecursively()
        out.mkdirs()
        out.setWritable(true, false)

        return NoopCompiledResult()
    }
}
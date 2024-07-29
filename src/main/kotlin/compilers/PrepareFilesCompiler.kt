package cz.lukynka.compilers

import java.io.File

class PrepareFilesCompiler(private var compiler: BasePackCompiler): Compiler {

    override fun compile(): CompiledResult {
        val out = File(compiler.path)
        out.deleteRecursively()
        out.mkdirs()

        return NoopCompiledResult()
    }
}
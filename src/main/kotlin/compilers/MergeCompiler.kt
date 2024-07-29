package cz.lukynka.compilers

import java.io.File

class MergeCompiler(private var compiler: BasePackCompiler): Compiler {

    override fun compile(): CompiledResult {
        val basePack = compiler.pack.basePack ?: return NoopCompiledResult()
        if(!basePack.exists()) throw Exception("Base pack file ${basePack.name} does not exist!")

        basePack.copyRecursively(File(compiler.path), true)
        return NoopCompiledResult()
    }
}
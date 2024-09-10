package cz.lukynka.youkai.compilers

import kotlinx.serialization.Serializable

interface Compiler {
    fun compile(): CompiledResult
}

interface CompiledResult {

}

@Serializable
class NoopCompiledResult: CompiledResult


package cz.lukynka.youkai.compilers

class GLSLObfuscator(private val compiler: BasePackCompiler): Compiler {

    override fun compile(): CompiledResult {
        //TODO load the shader file code
        //TODO figure out imports and linking to other files
        val glslCode = ""
        var obfuscatedCode = glslCode.replace("\\s+".toRegex(), "").replace("//.*".toRegex(), "")

        val variableNames = mutableMapOf<String, String>()
        val functionNames = mutableMapOf<String, String>()

        obfuscatedCode = obfuscatedCode.replace("([a-zA-Z_][a-zA-Z0-9_]*)".toRegex()) { match ->
            val originalName = match.value
            val obfuscatedName = if (originalName.startsWith("gl_")) {
                originalName
            } else {
                variableNames.getOrPut(originalName) { "v${variableNames.size}" }
            }
            obfuscatedName
        }

        //TODO write the code back into the file
        return GLSLObfuscationResult()
    }
}

class GLSLObfuscationResult(): CompiledResult
package cz.lukynka.compilers

import cz.lukynka.ResourceAtlas
import cz.lukynka.atlasFileContent
import kotlinx.serialization.Serializable
import java.io.File

class AtlasCompiler(val compiler: BasePackCompiler): Compiler {

    override fun compile(): CompiledResult {
        val atlasesPath = "${compiler.path}/assets/minecraft/atlases/"
        File(atlasesPath).mkdirs()
        val itemAtlas = File("$atlasesPath/items.json")
        val blockAtlas = File("$atlasesPath/blocks.json")
        itemAtlas.createNewFile()
        blockAtlas.createNewFile()
        val itemResourceAtlas = atlasFileContent(compiler.name)
        val blockResourceAtlas = atlasFileContent(compiler.name)
        itemAtlas.writeText(itemResourceAtlas.toJson())
        blockAtlas.writeText(blockResourceAtlas.toJson())
        return AtlasResult(listOf(itemResourceAtlas, blockResourceAtlas))
    }
}

@Serializable
data class AtlasResult(
    val atlases: List<ResourceAtlas>
): CompiledResult
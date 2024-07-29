package cz.lukynka.compilers

import cz.lukynka.ResourceAtlas
import cz.lukynka.atlasFileContent
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.io.File

class AtlasCompiler(val compiler: BasePackCompiler): Compiler {

    override fun compile(): CompiledResult {
        val atlasesPath = "${compiler.path}/assets/minecraft/atlases/"
        File(atlasesPath).mkdirs()
        val itemAtlas = File("$atlasesPath/items.json")
        val blockAtlas = File("$atlasesPath/blocks.json")

        val itemResourceAtlas = atlasFileContent(compiler.name)
        val blockResourceAtlas = atlasFileContent(compiler.name)

        val json = Json { ignoreUnknownKeys = true }

        // Merge atlases if exists
        if(itemAtlas.exists()) {
            val existingItemAtlas =  json.decodeFromString<ResourceAtlas>(itemAtlas.readText())
            existingItemAtlas.sources.forEach(itemResourceAtlas.sources::add)
        }

        if(blockAtlas.exists()) {
            val existingBlockAtlas =  json.decodeFromString<ResourceAtlas>(blockAtlas.readText())
            existingBlockAtlas.sources.forEach(blockResourceAtlas.sources::add)
        }

        itemAtlas.createNewFile()
        blockAtlas.createNewFile()
        itemAtlas.writeText(itemResourceAtlas.toJson())
        blockAtlas.writeText(blockResourceAtlas.toJson())
        return AtlasResult(listOf(itemResourceAtlas, blockResourceAtlas))
    }
}

@Serializable
data class AtlasResult(
    val atlases: List<ResourceAtlas>
): CompiledResult
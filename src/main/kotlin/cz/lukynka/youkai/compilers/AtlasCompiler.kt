package cz.lukynka.youkai.compilers

import cz.lukynka.cz.lukynka.youkai.ResourceAtlas
import cz.lukynka.cz.lukynka.youkai.atlasFileContent
import cz.lukynka.youkai.writers.writeTextAndClose
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
        itemAtlas.writeTextAndClose(itemResourceAtlas.toJson())
        blockAtlas.writeTextAndClose(blockResourceAtlas.toJson())
        return AtlasResult(listOf(itemResourceAtlas, blockResourceAtlas))
    }
}

@Serializable
data class AtlasResult(
    val atlases: List<ResourceAtlas>
): CompiledResult
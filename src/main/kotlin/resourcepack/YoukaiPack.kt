package cz.lukynka.resourcepack

import cz.lukynka.Config
import cz.lukynka.atlasFileContent
import cz.lukynka.objects.*
import cz.lukynka.prettylog.LogType
import cz.lukynka.prettylog.log
import java.io.File
import java.util.concurrent.atomic.AtomicInteger

class YoukaiPack(var name: String) {

    var compiledPackFileName = name
    var packFormat = PackMcMeta(PackFormat(PackVersion.MC_1_21, "Youkai Compiled Resourcepack"))

    var custom2dItems: MutableList<Custom2dItem> = mutableListOf()
    var customModels: MutableList<CustomModel> = mutableListOf()

    fun compile(path: String) {

        val customModelDataCount = AtomicInteger()
        val idToModelPathMap: MutableMap<Int, String> = mutableMapOf()

        val out = File(path)
        out.deleteRecursively()
        out.mkdirs()

        // Base Pack Stuff
        val packMcMeta = File("$path/pack.mcmeta")
        packMcMeta.writeText(packFormat.toJson())
        log(packFormat.toJson())
        log("Pack Format file created", LogType.DEBUG)

        // Atlases
        val atlasesPath = "$path/assets/minecraft/atlases/"
        File(atlasesPath).mkdirs()
        val itemAtlas = File("$atlasesPath/items.json")
        val blockAtlas = File("$atlasesPath/blocks.json")
        itemAtlas.createNewFile()
        blockAtlas.createNewFile()
        itemAtlas.writeText(atlasFileContent(name))
        blockAtlas.writeText(atlasFileContent(name))
        log("Atlases created", LogType.DEBUG)

        // Custom Models stuff

        val modelsDir = "$path/assets/minecraft/models/$name/youkai-compiled"
        File(modelsDir).mkdirs()

        val texturesDir = "$path/assets/minecraft/textures/$name/youkai-compiled"
        File(texturesDir).mkdirs()

        custom2dItems.shuffled().forEach {
            val id = customModelDataCount.getAndIncrement()
            val textureFile = "$texturesDir/${it.file.name}"
            val modelFile = "$modelsDir/${it.file.nameWithoutExtension}.json"
            it.file.copyTo(File(textureFile))
            val item = Item("item/generated", ItemTextures("${name}/youkai-compiled/${it.file.nameWithoutExtension}")).toJson()
            val file = File(modelFile)
            file.createNewFile()
            file.writeText(item)
            idToModelPathMap[id] = "${name}/youkai-compiled/${it.file.nameWithoutExtension}"
        }

        val baseCustomModelPath = "$path/assets/minecraft/models/item/"
        File(baseCustomModelPath).mkdirs()

        val overrides = mutableListOf<ItemOverride>()
        idToModelPathMap.forEach {
            overrides.add(ItemOverride(Predicate(it.key), it.value))
        }

        val item = Item("item/generated", ItemTextures("item/${Config.ITEM}"), overrides).toJson()
        val file = File("$baseCustomModelPath/${Config.ITEM}.json")
        file.createNewFile()
        file.writeText(item)

        log("custom models generated", LogType.DEBUG)
        log("Pack $name successfully generated!", LogType.SUCCESS)
    }
}
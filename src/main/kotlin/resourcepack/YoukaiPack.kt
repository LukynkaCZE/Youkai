package cz.lukynka.resourcepack

import cz.lukynka.Config
import cz.lukynka.atlasFileContent
import cz.lukynka.obfuscation.Obfuscatory
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

        val youkaiCompiledTextures = if(Config.OBFUSCATE) Obfuscatory.getNext() else "youkai-compiled"
        val youkaiCompiledModels = if(Config.OBFUSCATE) Obfuscatory.getNext() else "youkai-compiled"

        val customModelDataCount = AtomicInteger(1)
        val idToModelPathMap: MutableMap<Int, String> = mutableMapOf()
        val nameObf = if(Config.OBFUSCATE) Obfuscatory.getNext() else name

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
        itemAtlas.writeText(atlasFileContent(nameObf))
        blockAtlas.writeText(atlasFileContent(nameObf))
        log("Atlases created", LogType.DEBUG)

        // Custom Models stuff

        val modelsDir = "$path/assets/minecraft/models/$nameObf/$youkaiCompiledModels"
        File(modelsDir).mkdirs()

        val texturesDir = "$path/assets/minecraft/textures/$nameObf/$youkaiCompiledTextures"
        File(texturesDir).mkdirs()

        custom2dItems.shuffled().forEach {
            val id = customModelDataCount.getAndIncrement()
            val textureFile = "$texturesDir/${it.getAssetName()}"
            val modelFile = "$modelsDir/${it.getAssetNameWithoutExtension()}.json"
            it.file.copyTo(File(textureFile), true)
            val item = Item("item/generated", ItemTextures("${nameObf}/$youkaiCompiledTextures/${it.getAssetNameWithoutExtension()}")).toJson()
            val file = File(modelFile)
            file.createNewFile()
            file.writeText(item)
            idToModelPathMap[id] = "${nameObf}/$youkaiCompiledModels/${it.getAssetNameWithoutExtension()}"
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
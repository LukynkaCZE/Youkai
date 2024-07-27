package cz.lukynka.compilers

import cz.lukynka.Config
import cz.lukynka.api.YoukaiServerModel
import cz.lukynka.objects.Item
import cz.lukynka.objects.ItemOverride
import cz.lukynka.objects.ItemTextures
import cz.lukynka.objects.Predicate
import kotlinx.serialization.Serializable
import java.io.File
import java.util.concurrent.atomic.AtomicInteger

class CustomModelsCompiler(var compiler: BasePackCompiler): Compiler {

    val customModelDataCount = AtomicInteger(1)
    val idToModelPathMap: MutableMap<Int, String> = mutableMapOf()
    val serverModels = mutableListOf<YoukaiServerModel>()
    
    override fun compile(): CompiledResult {

        val modelsDir = "${compiler.path}/assets/minecraft/models/${compiler.name}/${compiler.youkaiCompiledModels}"
        File(modelsDir).mkdirs()

        val texturesDir = "${compiler.path}/assets/minecraft/textures/${compiler.name}/${compiler.youkaiCompiledTextures}"
        File(texturesDir).mkdirs()

        compiler.pack.custom2dItems.shuffled().forEach {
            val id = customModelDataCount.getAndIncrement()
            val textureFile = "$texturesDir/${it.getAssetName()}"
            val modelFile = "$modelsDir/${it.getAssetNameWithoutExtension()}.json"
            it.file.copyTo(File(textureFile), true)
            val item = Item("item/generated", ItemTextures("${compiler.name}/${compiler.youkaiCompiledTextures}/${it.getAssetNameWithoutExtension()}")).toJson()
            val file = File(modelFile)
            file.createNewFile()
            file.writeText(item)
            idToModelPathMap[id] = "${compiler.name}/${compiler.youkaiCompiledModels}/${it.getAssetNameWithoutExtension()}"
            serverModels.add(YoukaiServerModel(it.getNonObfAssetName(), id, Config.ITEM))
        }

        val baseCustomModelPath = "${compiler.path}/assets/minecraft/models/item/"
        File(baseCustomModelPath).mkdirs()

        val overrides = mutableListOf<ItemOverride>()
        idToModelPathMap.forEach {
            overrides.add(ItemOverride(Predicate(it.key), it.value))
        }
        val item = Item("item/generated", ItemTextures("item/${Config.ITEM}"), overrides).toJson()
        val file = File("$baseCustomModelPath/${Config.ITEM}.json")
        file.createNewFile()
        file.writeText(item)
        return CustomModelResponse(serverModels)
    }
}

@Serializable
data class CustomModelResponse(
    val items: MutableList<YoukaiServerModel>
): CompiledResult
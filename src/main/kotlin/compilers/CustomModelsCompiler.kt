package cz.lukynka.compilers

import cz.lukynka.Config
import cz.lukynka.api.YoukaiServerModel
import cz.lukynka.minecraft.MinecraftModel
import cz.lukynka.objects.Item
import cz.lukynka.objects.ItemOverride
import cz.lukynka.objects.ItemTextures
import cz.lukynka.objects.Predicate
import cz.lukynka.shuffled
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.util.concurrent.atomic.AtomicInteger

class CustomModelsCompiler(private var compiler: BasePackCompiler): Compiler {

    private var outModelList: MutableMap<String, String> = mutableMapOf()
    private val serverModels = mutableListOf<YoukaiServerModel>()
    
    override fun compile(): CompiledResult {

        val modelsDir = "${compiler.path}/assets/minecraft/models/${compiler.name}/${compiler.youkaiCompiledModels}"
        File(modelsDir).mkdirs()

        val texturesDir = "${compiler.path}/assets/minecraft/textures/${compiler.name}/${compiler.youkaiCompiledTextures}"
        File(texturesDir).mkdirs()

        compiler.pack.custom2dItems.shuffled().forEach {
            val textureFile = "$texturesDir/${it.getAssetName()}"
            val modelFile = "$modelsDir/${it.getAssetNameWithoutExtension()}.json"
            it.file.copyTo(File(textureFile), true)
            val item = Item("item/generated", ItemTextures("${compiler.name}/${compiler.youkaiCompiledTextures}/${it.getAssetNameWithoutExtension()}")).toJson()
            val file = File(modelFile)
            file.createNewFile()
            file.writeText(item)
            outModelList[it.getNonObfAssetNameWithoutExtension()] = "${compiler.name}/${compiler.youkaiCompiledModels}/${it.getAssetNameWithoutExtension()}"
        }

        compiler.pack.custom3dModels.forEach {
            val textureFile = "$texturesDir/${it.getTextureAssetName()}"
            val modelFile = "$modelsDir/${it.getModelAssetName()}"

            it.textureFile.copyTo(File(textureFile))

            val json = Json {ignoreUnknownKeys = true}
            val minecraftModel = json.decodeFromString<MinecraftModel>(it.modelFile.readText())
            minecraftModel.textures?.set("0", "${compiler.name}/${compiler.youkaiCompiledTextures}/${it.getTextureAssetNameWithoutExtension()}")
            val outModelFile = json.encodeToString<MinecraftModel>(minecraftModel)
            val file = File(modelFile)
            file.createNewFile()
            file.writeText(outModelFile)
            outModelList[it.modelFile.nameWithoutExtension] = "${compiler.name}/${compiler.youkaiCompiledModels}/${it.getModelAssetNameWithoutExtension()}"
//            serverModels.add(YoukaiServerModel(it.modelFile.nameWithoutExtension, null, Config.ITEM))
        }

        val baseCustomModelPath = "${compiler.path}/assets/minecraft/models/item/"
        File(baseCustomModelPath).mkdirs()
        val overrides = mutableListOf<ItemOverride>()
        val atomicId = AtomicInteger(1)
        if(Config.SHUFFLE_IDS_ON_COMPILE) outModelList = outModelList.shuffled().toMutableMap()
        outModelList.forEach { model ->
            val id = atomicId.getAndIncrement()
            overrides.add(ItemOverride(Predicate(id), model.value))
            serverModels.add(YoukaiServerModel(model.key, id, Config.ITEM))
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
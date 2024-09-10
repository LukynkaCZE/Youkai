package cz.lukynka.youkai.compilers

import cz.lukynka.youkai.YoukaiServerModel
import cz.lukynka.youkai.minecraft.MinecraftModel
import cz.lukynka.youkai.objects.Item
import cz.lukynka.youkai.objects.ItemOverride
import cz.lukynka.youkai.objects.ItemTextures
import cz.lukynka.youkai.objects.Predicate
import cz.lukynka.cz.lukynka.youkai.shuffled
import cz.lukynka.youkai.config.ConfigManager
import cz.lukynka.youkai.writers.writeTextAndClose
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.util.concurrent.atomic.AtomicInteger

class CustomModelsCompiler(private var compiler: BasePackCompiler): Compiler {

    private var outModelList: MutableMap<String, String> = mutableMapOf()
    private val serverModels = mutableListOf<YoukaiServerModel>()

    val config = ConfigManager.currentConfig.compiler
    
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
            file.writeTextAndClose(item)
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
            file.writeTextAndClose(outModelFile)
            outModelList[it.modelFile.nameWithoutExtension] = "${compiler.name}/${compiler.youkaiCompiledModels}/${it.getModelAssetNameWithoutExtension()}"
        }

        val baseCustomModelPath = "${compiler.path}/assets/minecraft/models/item/"
        File(baseCustomModelPath).mkdirs()
        val overrides = mutableListOf<ItemOverride>()
        val atomicId = AtomicInteger(1)
        if(config.shuffleIds) outModelList = outModelList.shuffled().toMutableMap()
        outModelList.forEach { model ->
            val id = atomicId.getAndIncrement()
            overrides.add(ItemOverride(Predicate(id), model.value))
            serverModels.add(YoukaiServerModel(model.key, id, config.baseItem))
        }

        val item = Item("item/generated", ItemTextures("item/${config.baseItem}"), overrides).toJson()
        val file = File("$baseCustomModelPath/${config.baseItem}.json")
        file.createNewFile()
        file.writeTextAndClose(item)
        return CustomModelResponse(serverModels)
    }
}

@Serializable
data class CustomModelResponse(
    val items: MutableList<YoukaiServerModel>
): CompiledResult
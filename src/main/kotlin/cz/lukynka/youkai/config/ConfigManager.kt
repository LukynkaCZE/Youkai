package cz.lukynka.youkai.config

import com.akuleshov7.ktoml.Toml
import cz.lukynka.prettylog.LogType
import cz.lukynka.prettylog.log
import cz.lukynka.youkai.obfuscation.ObfuscationStrategy
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import java.io.File
import kotlin.system.exitProcess

object ConfigManager {

    val configFile = File("youkai.toml")
    var currentConfig: YoukaiConfig = YoukaiConfig()

    val toml = Toml()

    fun load() {
        log("Loading youkai config file..", LogType.CONFIG)

        //make one if it doesn't exist
        if(!configFile.exists()) save(YoukaiConfig())
        try {
            val config = toml.decodeFromString<YoukaiConfig>(configFile.readText())
            currentConfig = config
        } catch (ex: Exception) {
            log("There was an error while loading your youkai config file", LogType.FATAL)
            log(ex)
            exitProcess(0)
        }
    }

    fun save(config: YoukaiConfig) {
        val text = toml.encodeToString<YoukaiConfig>(config)
        configFile.writeText(text)
    }
}

@Serializable
data class YoukaiConfig(
    @SerialName("General")
    val general: GeneralConfig = GeneralConfig(),
    @SerialName("Compiler")
    val compiler: CompilerConfig = CompilerConfig(),
    @SerialName("Publishing")
    val publishing: Publishing = Publishing()
)

@Serializable
data class GeneralConfig(
    val auth: Boolean = true,
    val sendDataUrls: MutableList<String> = mutableListOf(),
    val youkaiToken: String = ""
)

@Serializable
data class CompilerConfig(
    val baseItem: String = "raw_copper",
    val obfuscation: Boolean = true,
    val shuffleIds: Boolean = true,
    val obfuscationStrategy: ObfuscationStrategy = ObfuscationStrategy.BASE64
)

@Serializable
data class Publishing(
    val publish: Boolean = false,
    val publishingService: PublishingApis = PublishingApis.CLOUDFLARE_R2,
    @SerialName("CloudflareR2")
    val cloudflareR2: CloudflareR2 = CloudflareR2(),
    @SerialName("AmazonS3")
    val amazonS3: AmazonS3 = AmazonS3()
)

@Serializable
data class CloudflareR2(
    val accessKey: String = "",
    val secretAccessKey: String = "",
    val url: String = "",
    val region: String = "",
    val bucket: String = ""
)

@Serializable
data class AmazonS3(
    val accessKey: String = "",
    val secretAccessKey: String = "",
    val url: String = "",
    val region: String = "",
    val bucket: String = ""
)

@Serializable
enum class PublishingApis {
    CLOUDFLARE_R2,
    AMAZON_S3
}
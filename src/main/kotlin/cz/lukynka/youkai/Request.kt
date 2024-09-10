package cz.lukynka.youkai

import cz.lukynka.prettylog.LogType
import cz.lukynka.prettylog.log
import cz.lukynka.youkai.config.ConfigManager
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.lang.Exception
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpRequest.BodyPublishers
import java.net.http.HttpResponse.BodyHandlers

fun sendCompiledPackRequest(sync: YoukaiSync) {
    val client = HttpClient.newHttpClient()

    val json = Json { ignoreUnknownKeys = true }
    val body = json.encodeToString<YoukaiSync>(sync)

    val config = ConfigManager.currentConfig.general

    config.sendDataUrls.forEach {
        try {
            val request = HttpRequest.newBuilder()
                .POST(BodyPublishers.ofString(body))
                .uri(URI(it))
                .header("Authorization", ConfigManager.currentConfig.general.youkaiToken)
                .build()
            client.send(request, BodyHandlers.ofString())
        } catch (ex: Exception){
            log("Sending pack data to $it failed: ${ex.message}", LogType.ERROR)
        }
    }
}

@Serializable
data class YoukaiSync(
    val models: List<YoukaiServerModel>
)
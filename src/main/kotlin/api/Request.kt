package cz.lukynka.api

import cz.lukynka.Config
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpRequest.BodyPublishers
import java.net.http.HttpResponse.BodyHandlers

fun sendCompiledPackRequest(sync: YoukaiSync) {
    val client = HttpClient.newHttpClient()

    val json = Json { ignoreUnknownKeys = true }
    val body = json.encodeToString<YoukaiSync>(sync)

    Config.PACK_DATA_REQUESTS.forEach {
        val request = HttpRequest.newBuilder()
           .POST(BodyPublishers.ofString(body))
           .uri(URI(it))
           .header("Authorization", Config.TOKEN)
            .build()
        client.send(request, BodyHandlers.ofString())
    }
}

@Serializable
data class YoukaiSync(
    val models: List<YoukaiServerModel>
)
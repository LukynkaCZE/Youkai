package cz.lukynka.youkai.publishing

import cz.lukynka.youkai.config.ConfigManager
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.s3.S3Client
import software.amazon.awssdk.services.s3.model.PutObjectRequest
import java.io.File
import java.net.URI

class CloudflareR2Publisher: YouikaiResourcepackPublisher {

    private val config = ConfigManager.currentConfig.publishing.cloudflareR2

    private val credentials = StaticCredentialsProvider.create(AwsBasicCredentials.builder()
        .accessKeyId(config.accessKey)
        .secretAccessKey(config.secretAccessKey)
        .build()
    )

    private val client = S3Client.builder()
        .credentialsProvider(credentials)
        .region(Region.of(config.region))
        .endpointOverride(URI(config.url))
        .build()

    override fun push(file: File) {
        val putObjectRequest = PutObjectRequest.builder()
            .bucket(config.bucket)
            .key(file.name)
            .build()

        client.putObject(putObjectRequest, RequestBody.fromFile(file))
    }
}
package hackathon.sprinter.aws.model

data class UploadResponse(
    val response: String,
    val id: Long,
    val url: String,
    val name: String,
)

data class AwsS3ObjectDto(
    val id: Long,
    val url: String,
)
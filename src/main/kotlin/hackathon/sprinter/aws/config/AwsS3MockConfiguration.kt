package hackathon.sprinter.aws.config

import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.client.builder.AwsClientBuilder
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import hackathon.sprinter.aws.repository.AwsS3Repository
import hackathon.sprinter.aws.service.AwsS3Service
import org.springframework.beans.factory.annotation.Qualifier
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Profile

@Configuration
@Profile("local", "test")
class AwsS3MockConfiguration {

    @Value("\${aws.s3.minio.region}")
    private val region: String = ""

    @Value("\${aws.s3.minio.endpoint}")
    private val endpoint: String = ""

    @Value("\${aws.s3.minio.accessKey}")
    private val accessKey: String = ""

    @Value("\${aws.s3.minio.secretKey}")
    private val secretKey: String = ""

    @Value("\${aws.s3.bucket.sprinter-img-bucket}")
    private val sprinterImgBucket: String = ""

    @Bean("awsS3")
    @Primary
    fun awsMockS3Client(): AmazonS3 {
        return AmazonS3ClientBuilder
            .standard()
            .withEndpointConfiguration(AwsClientBuilder.EndpointConfiguration(endpoint, region))
            .withCredentials(AWSStaticCredentialsProvider(BasicAWSCredentials(accessKey, secretKey)))
            .withPathStyleAccessEnabled(true)
            .build()
    }

    @Bean("s3Service")
    @Primary
    fun awsMockS3Service(
        @Qualifier("awsS3") awsMockS3Client: AmazonS3,
        awsS3Repository: AwsS3Repository
    ): AwsS3Service {
        return AwsS3Service(awsMockS3Client, awsS3Repository)
    }
}
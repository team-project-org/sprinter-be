package hackathon.sprinter.aws.config

import com.amazonaws.auth.AWSCredentials
import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.regions.Regions
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
@Profile("production")
class AwsS3Configuration {
    @Value("\${aws.s3-bucket.access-key}")
    private lateinit var accessKey: String

    @Value("\${aws.s3-bucket.secret-key}")
    private lateinit var secretKey: String

    @Value("\${aws.s3-bucket.region}")
    private lateinit var regionName: String

    @Bean("awsS3")
    @Primary
    fun awsS3Client(): AmazonS3 {
        val awsCredentials: AWSCredentials = BasicAWSCredentials(accessKey, secretKey)
        val region = Regions.fromName(regionName)

        return AmazonS3ClientBuilder
            .standard()
            .withRegion(region)
            .withCredentials(AWSStaticCredentialsProvider(awsCredentials))
            .build()
    }

    @Bean("s3Service")
    @Primary
    fun awsS3Service(
        @Qualifier("awsS3") awsS3Client: AmazonS3,
        awsS3Repository: AwsS3Repository
    ): AwsS3Service {
        return AwsS3Service(awsS3Client, awsS3Repository)
    }
}
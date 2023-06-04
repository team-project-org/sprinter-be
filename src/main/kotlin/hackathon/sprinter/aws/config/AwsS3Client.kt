package hackathon.sprinter.aws.config

import com.amazonaws.auth.AWSCredentials
import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.regions.Regions
import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class AwsS3Client {
    @Value("\${aws.s3-bucket.access-key}")
    private lateinit var accessKey: String

    @Value("\${aws.s3-bucket.secret-key}")
    private lateinit var secretKey: String

    @Value("\${aws.s3-bucket.region}")
    private lateinit var regionName: String

    fun getAwsS3Client(): AmazonS3 {
        val awsCredentials: AWSCredentials = BasicAWSCredentials(accessKey, secretKey)
        val region = Regions.fromName(regionName)

        return AmazonS3ClientBuilder
            .standard()
            .withRegion(region)
            .withCredentials(AWSStaticCredentialsProvider(awsCredentials))
            .build()
    }
}
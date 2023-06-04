package hackathon.sprinter.aws.repository

import hackathon.sprinter.aws.model.AwsS3Object
import org.springframework.data.jpa.repository.JpaRepository

interface AwsS3Repository : JpaRepository<AwsS3Object, Long>
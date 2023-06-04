package hackathon.sprinter.aws.model

import hackathon.sprinter.configure.dto.BaseTimeEntity
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "aws_s3_object")
class AwsS3Object(
    @Column(unique = true, nullable = false) val url: String,
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long = 0L,
): BaseTimeEntity()
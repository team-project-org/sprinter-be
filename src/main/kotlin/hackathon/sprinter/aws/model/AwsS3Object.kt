package hackathon.sprinter.aws.model

import hackathon.sprinter.configure.dto.BaseTimeEntity
import javax.persistence.*

@Entity
@Table(name = "aws_s3_objects")
class AwsS3Object(
    @Column(unique = true, nullable = false) val url: String,
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long = 0L,
): BaseTimeEntity()
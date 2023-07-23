package hackathon.sprinter.tag.model

import hackathon.sprinter.configure.dto.BaseTimeEntity
import hackathon.sprinter.util.TagType
import javax.persistence.*

@Entity
@Table(name = "tags")
class Tag(
    @Column(nullable = false) @Enumerated(EnumType.STRING) val tagType: TagType,
    @Column(nullable = false) var count: Int = 0,
    @Column(nullable = false) val name: String,
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long = 0L,
) : BaseTimeEntity()
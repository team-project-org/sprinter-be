package hackathon.sprinter.sprint.model

import hackathon.sprinter.configure.dto.BaseEntity
import java.time.OffsetDateTime
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Entity
class Sprint(
    @Column(nullable = false) val dateSprintStart: OffsetDateTime,
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long = 0L,
) : BaseEntity() {
}
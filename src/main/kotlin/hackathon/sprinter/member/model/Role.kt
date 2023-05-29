package hackathon.sprinter.member.model

import org.hibernate.envers.Audited
import javax.persistence.Column
import javax.persistence.Embeddable
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id

@Embeddable
data class Role(
    @Column(nullable = false) @Enumerated(EnumType.STRING) val roleType: RoleType
)

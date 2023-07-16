package hackathon.sprinter.member.model

import hackathon.sprinter.util.RoleType
import hackathon.sprinter.util.RoleTypeEnumConverter
import javax.persistence.Column
import javax.persistence.Convert
import javax.persistence.Embeddable

@Embeddable
data class Role(
    @Column(nullable = false) @Convert(converter = RoleTypeEnumConverter::class) val roleType: RoleType
)

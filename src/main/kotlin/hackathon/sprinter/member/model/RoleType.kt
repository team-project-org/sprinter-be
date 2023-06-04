package hackathon.sprinter.member.model

import org.springframework.security.access.AuthorizationServiceException
import javax.persistence.AttributeConverter
import javax.persistence.Converter

enum class RoleType {
    ROLE_USER, ROLE_ADMIN;

    companion object {
        fun find(roleName: String): RoleType {
            return RoleType.values().firstOrNull { it.name == roleName }
                ?: throw AuthorizationServiceException("해당하는 권한이 없습니다.")
        }
    }
}

@Converter(autoApply = true)
class RoleTypeEnumConverter : AttributeConverter<RoleType, String> {
    override fun convertToEntityAttribute(dbCode: String): RoleType {
        return RoleType.find(dbCode)
    }

    override fun convertToDatabaseColumn(type: RoleType?): String? {
        return type?.name
    }
}

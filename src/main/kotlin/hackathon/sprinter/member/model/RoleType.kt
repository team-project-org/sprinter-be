package hackathon.sprinter.member.model

import org.springframework.security.access.AuthorizationServiceException

enum class RoleType {
    ROLE_USER, ROLE_ADMIN;

    companion object {
        fun find(roleName: String): RoleType {
            return RoleType.values().firstOrNull { it.name == roleName }
                ?: throw AuthorizationServiceException("해당하는 권한이 없습니다.")
        }
    }
}

package hackathon.sprinter.member.service

import hackathon.sprinter.member.model.Role
import hackathon.sprinter.member.model.RoleType
import hackathon.sprinter.member.repository.RoleJpaRepository
import org.springframework.security.access.AuthorizationServiceException
import org.springframework.stereotype.Service

@Service
class RoleService(
    private val roleJpaRepository: RoleJpaRepository
) {
    fun findRole(roleType: RoleType): Role {
        return roleJpaRepository.findByRoleType(roleType)
            ?: throw AuthorizationServiceException("해당하는 권한이 없습니다.")
    }
}

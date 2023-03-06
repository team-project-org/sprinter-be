package hackathon.peerfund.member.service

import hackathon.peerfund.member.model.Role
import hackathon.peerfund.member.model.RoleType
import hackathon.peerfund.member.repository.RoleJpaRepository
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
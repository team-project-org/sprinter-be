package hackathon.sprinter.member.repository

import hackathon.sprinter.member.model.Role
import hackathon.sprinter.member.model.RoleType
import org.springframework.data.jpa.repository.JpaRepository

interface RoleJpaRepository : JpaRepository<Role, Long> {
    fun findByRoleType(roleType: RoleType): Role?
}

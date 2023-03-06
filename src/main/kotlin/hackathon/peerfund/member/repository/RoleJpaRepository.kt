package hackathon.peerfund.member.repository

import hackathon.peerfund.member.model.Role
import hackathon.peerfund.member.model.RoleType
import org.springframework.data.jpa.repository.JpaRepository

interface RoleJpaRepository : JpaRepository<Role, Long> {
    fun findByRoleType(roleType: RoleType): Role?
}
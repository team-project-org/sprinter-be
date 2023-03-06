package hackathon.peerfund.member.repository

import hackathon.peerfund.member.model.Member
import org.springframework.data.jpa.repository.JpaRepository

interface MemberJpaRepository : JpaRepository<Member, Long> {
    fun findMemberByToken(token: String): Member?
}
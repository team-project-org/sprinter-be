package hackathon.sprinter.member.repository

import hackathon.sprinter.member.model.Member
import org.springframework.data.jpa.repository.JpaRepository

interface MemberJpaRepository : JpaRepository<Member, Long> {
    fun findMemberByToken(token: String): Member?
}

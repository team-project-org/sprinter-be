package hackathon.sprinter.member.repository

import hackathon.sprinter.member.model.Member
import hackathon.sprinter.member.model.QMember.member
import hackathon.sprinter.profile.model.QProfile.profile
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@Repository
class MemberRepository(
    memberJpaRepository: MemberJpaRepository
) : QuerydslRepositorySupport(Member::class.java), MemberJpaRepository by memberJpaRepository {

    @PersistenceContext
    override fun setEntityManager(entityManager: EntityManager) {
        super.setEntityManager(entityManager)
    }

    @Transactional(readOnly = true)
    fun findMemberById(id: Long): Member? {
        return from(member)
            .where(member.id.eq(id))
            .fetchOne()
    }

    fun findMemberByUsername(username: String): Member? {
        return from(member)
            .where(member.username.eq(username))
            .fetchOne()
    }

    fun findAllMember(): List<Member> {
        return from(member)
            .orderBy(member.dateCreated.desc())
            .fetch()
    }

    fun findMockMember(): List<Member> {
        return from(member)
            .leftJoin(member.profile, profile).fetchJoin()
            .where(member.isMock.eq(true))
            .where(member.dateDeleted.isNull)
            .fetch()
    }
}

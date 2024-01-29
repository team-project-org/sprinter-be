package hackathon.sprinter.profile.repository.mysql

import hackathon.sprinter.profile.model.Link
import hackathon.sprinter.profile.model.QLink.link
import hackathon.sprinter.profile.repository.LinkJpaRepository
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext

@Repository
class LinkRepository(
    private val linkJpaRepository: LinkJpaRepository
) : QuerydslRepositorySupport(LinkRepository::class.java),
    LinkJpaRepository by linkJpaRepository {

    @PersistenceContext
    override fun setEntityManager(entityManager: EntityManager) {
        super.setEntityManager(entityManager)
    }

    fun findPortfolioLinkListInProfileIds(profileIds: List<Long>): List<Link> {
        return from(link)
            .where(link.profile.id.`in`(profileIds))
            .fetch()
    }
}
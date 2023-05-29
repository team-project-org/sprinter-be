package hackathon.sprinter.post.model

import hackathon.sprinter.configure.dto.BaseEntity
import hackathon.sprinter.member.model.Member
import org.hibernate.envers.Audited
import org.hibernate.envers.NotAudited
import java.time.OffsetDateTime
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.ManyToOne
import javax.persistence.OneToMany

@Entity
@Audited
class Post(
    @Column(nullable = false) val title: String,
    @Column(nullable = false) val startDate: OffsetDateTime,
    @Column(nullable = false) val endDate: OffsetDateTime,
    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY)
    @NotAudited
    val memberPostList: MutableList<MemberPost> = mutableListOf(),
    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    @NotAudited
    val ownerMember: Member,
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long = 0L,
) : BaseEntity() {

    fun isOwner(member: Member): Boolean {
        return this.ownerMember.id == member.id
    }
}
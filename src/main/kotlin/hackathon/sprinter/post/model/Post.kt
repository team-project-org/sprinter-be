package hackathon.sprinter.post.model

import hackathon.sprinter.configure.dto.BaseTimeEntity
import hackathon.sprinter.member.model.Member
import java.time.OffsetDateTime
import javax.persistence.*

@Entity
@Table(name ="posts")
class Post(
    @Column(nullable = false) val title: String,
    @Column(nullable = false) val startDate: OffsetDateTime?,
    @Column(nullable = false) val endDate: OffsetDateTime?,
    @OneToMany(mappedBy = "post", fetch = FetchType.LAZY)
    val memberPostList: MutableList<MemberPost> = mutableListOf(),
    @ManyToOne(fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    var ownerMember: Member? = null,
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long = 0L,
) : BaseTimeEntity() {

    fun isOwner(member: Member): Boolean {
        return this.ownerMember?.id == member.id
    }

    fun changeOwner(member: Member): Boolean {
        this.ownerMember = member
        return true
    }
}
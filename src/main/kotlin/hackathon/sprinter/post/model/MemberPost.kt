package hackathon.sprinter.post.model

import hackathon.sprinter.configure.dto.BaseTimeEntity
import hackathon.sprinter.member.model.Member
import javax.persistence.*

@Entity
@Table(name ="member_posts")
class MemberPost(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    var member: Member,
    @ManyToOne(cascade = [CascadeType.ALL], fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    var post: Post,
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long = 0L,
) : BaseTimeEntity() {
}
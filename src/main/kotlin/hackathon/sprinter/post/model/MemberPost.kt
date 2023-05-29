package hackathon.sprinter.post.model

import hackathon.sprinter.configure.dto.BaseTimeEntity
import hackathon.sprinter.member.model.Member
import org.hibernate.envers.Audited
import javax.persistence.CascadeType
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne

@Entity
@Audited
class MemberPost(
    @ManyToOne
    @JoinColumn(name = "member_id")
    var member: Member,
    @ManyToOne(cascade = [CascadeType.ALL])
    @JoinColumn(name = "post_id")
    var post: Post,
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long = 0L,
) : BaseTimeEntity() {
}
package hackathon.sprinter.member.model

import hackathon.sprinter.configure.dto.BaseTimeEntity
import hackathon.sprinter.post.model.MemberPost
import hackathon.sprinter.post.model.Post
import hackathon.sprinter.profile.model.Profile
import hackathon.sprinter.util.RoleType
import java.io.Serializable
import javax.persistence.*

@Entity
@Table(name ="members")
class Member(
    @Column(nullable = false) val username: String,
    @Column(nullable = false) val password: String,
    @Column(nullable = false) var token: String,
    @Column(nullable = false) var profileName: String,
    @ElementCollection(fetch = FetchType.EAGER) val roles: MutableCollection<Role> = mutableListOf(),
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long = 0L,
) : BaseTimeEntity(), Serializable {

    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    val memberPostList: MutableList<MemberPost> = mutableListOf()

    @OneToMany(mappedBy = "ownerMember", fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    val ownerPostList: MutableList<Post> = mutableListOf()

    @OneToOne(mappedBy = "member", fetch = FetchType.LAZY, cascade = [CascadeType.ALL], orphanRemoval = true)
    private lateinit var profile: Profile

    fun updateMyProfile(profile: Profile) {
        profile.member = this
        this.profile = profile
    }

    fun updateToken(token: String) {
        this.token = token
    }

    fun getRoleTypeList(): List<RoleType> {
        return roles.map { it.roleType }
    }

    fun addRoleType(roleType: RoleType) {
        this.roles.add(Role(roleType))
    }

    fun createPost(memberPost: MemberPost, post: Post): Post {
        this.memberPostList.add(memberPost)
        post.memberPostList.add(memberPost)
        memberPost.member = this
        memberPost.post = post
        this.ownerPostList.add(post)
        post.ownerMember = this

        return post
    }
}

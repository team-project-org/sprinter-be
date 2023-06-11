package hackathon.sprinter.member.model

import hackathon.sprinter.configure.dto.BaseEntity
import hackathon.sprinter.post.model.MemberPost
import hackathon.sprinter.post.model.Post
import java.io.Serializable
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.ElementCollection
import javax.persistence.Entity
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.OneToMany

@Entity
class Member(
    @Column(nullable = false) val username: String,
    @Column(nullable = false) val password: String,
    @Column(nullable = false) var token: String,
    @Column(nullable = false) var profileName: String,
    @ElementCollection(fetch = FetchType.EAGER) val roleList: MutableList<Role> = mutableListOf(),
    @OneToMany(mappedBy = "member", fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    val memberPostList: MutableList<MemberPost> = mutableListOf(),
    @OneToMany(mappedBy = "ownerMember", fetch = FetchType.LAZY, cascade = [CascadeType.ALL])
    val ownerPostList: MutableList<Post> = mutableListOf(),
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long = 0L,
) : BaseEntity(), Serializable {

    fun updateToken(token: String) {
        this.token = token
    }

    fun getRoleTypeList(): List<RoleType> {
        return roleList.map { it.roleType }
    }

    fun addRoleType(roleType: RoleType) {
        this.roleList.add(Role(roleType))
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

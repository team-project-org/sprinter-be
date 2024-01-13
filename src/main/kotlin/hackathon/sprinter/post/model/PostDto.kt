package hackathon.sprinter.post.model

import hackathon.sprinter.member.model.Member
import hackathon.sprinter.util.RoleType
import java.time.OffsetDateTime
import java.util.LinkedList

data class PostDto(
    val id: String,
    val title: String,
    val startDate: OffsetDateTime?,
    val endDate: OffsetDateTime?,
    val owner: MemberDto?,
) {
    private val queue = LinkedList<Long>()
    init {
        println("PostDto init")
        queue.addAll(listOf(1L, 2L, 3L, 4L, 5L))
        for (i in 0..5) {
            println("i: $i")
        }
        val name = String
        val myTallLength = 123
        println(myTallLength)

        val queue1 = getQueue()
        queue1.map { println(it) }
    }

    fun getQueue(): LinkedList<Long> {
        return queue
    }

    companion object {
        fun from(post: Post): PostDto {
            return PostDto(
                id = post.id.toString(),
                title = post.title,
                startDate = post.startDate,
                endDate = post.endDate,
                owner = MemberDto.from(post.ownerMember),
            )
        }
    }
}

data class MemberDto(
    val id: Long,
    val username: String,
    val profileName: String,
    val roleTypeList: List<RoleType>
) {
    companion object {
        fun from(member: Member?): MemberDto? {
            if (member == null) return null
            return MemberDto(
                id = member.id,
                username = member.username,
                profileName = member.profileName,
                roleTypeList = member.roles.map { it.roleType }
            )
        }
    }
}

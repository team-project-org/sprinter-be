package hackathon.sprinter.post.model

import hackathon.sprinter.member.model.Member
import hackathon.sprinter.member.model.RoleType
import java.time.OffsetDateTime

data class PostDto(
    val title: String,
    val startDate: OffsetDateTime,
    val endDate: OffsetDateTime,
    val owner: MemberDto,
) {
    companion object {
        fun from(post: Post): PostDto {
            return PostDto(
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
        fun from(member: Member): MemberDto {
            return MemberDto(
                id = member.id,
                username = member.username,
                profileName = member.profileName,
                roleTypeList = member.roleList.map { it.roleType }
            )
        }
    }
}

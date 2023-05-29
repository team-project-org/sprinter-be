package hackathon.sprinter.util

import com.netflix.dgs.codegen.generated.types.Member
import com.netflix.dgs.codegen.generated.types.Post
import hackathon.sprinter.post.model.MemberDto
import hackathon.sprinter.post.model.PostDto

fun PostDto.toGqlSchema(): Post {
    return Post(
        title = this.title,
        start_date = this.startDate,
        end_date = this.endDate,
        owner = this.owner.toGqlSchema()
    )
}

fun hackathon.sprinter.member.model.Member.toGqlSchema(): Member {
    return Member(
        id = this.id.toString(),
        username = this.username,
        profile_name = this.profileName,
        role_type_list = this.roleList.map { it.roleType },
    )
}
fun MemberDto.toGqlSchema(): Member {
    return Member(
        id = this.id.toString(),
        username = this.username,
        profile_name = this.profileName,
        role_type_list = this.roleTypeList
    )
}
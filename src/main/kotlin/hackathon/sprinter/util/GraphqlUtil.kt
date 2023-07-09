package hackathon.sprinter.util

import com.netflix.dgs.codegen.generated.types.MemberResponse
import com.netflix.dgs.codegen.generated.types.PostResponse
import hackathon.sprinter.member.model.Member
import hackathon.sprinter.post.model.Post

fun Post.toGqlSchema(): PostResponse {
    return PostResponse(
        id = this.id.toString(),
        title = this.title,
        start_date = this.startDate,
        end_date = this.endDate,
        owner = this.ownerMember?.toGqlSchema()
    )
}

fun Member.toGqlSchema(): MemberResponse {
    return MemberResponse(
        id = this.id.toString(),
        username = this.username,
        profile_name = this.profileName,
        role_type_list = this.roleList.map { it.roleType },
    )
}

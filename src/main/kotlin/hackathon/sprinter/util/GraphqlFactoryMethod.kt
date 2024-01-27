package hackathon.sprinter.util

import com.netflix.dgs.codegen.generated.types.MockMemberResponse
import hackathon.sprinter.mockmember.model.MockMember

fun MockMember.toGqlSchema(): MockMemberResponse {
    return MockMemberResponse(
        email = email,
        name = name,
        job = job,
        profile_image_url = profileImageUrl,
        portfolio_link_url_list = portfolioLinkList,
    )
}
package hackathon.sprinter.profile.resolver

import com.netflix.dgs.codegen.generated.types.ProfileResponse
import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsQuery
import hackathon.sprinter.profile.service.ProfileQueryService

@DgsComponent
class ProfileQueryResolver(
    private val profileQueryService: ProfileQueryService,
) {
    @DgsQuery
    fun getMyProfile(): ProfileResponse {
        return profileQueryService.getMyProfile()
    }
}
package hackathon.sprinter.post.controller

import com.netflix.dgs.codegen.generated.types.CreatePostInput
import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsMutation
import hackathon.sprinter.post.service.PostMutationService

@DgsComponent
class PostMutationResolver(
    private val postMutationService: PostMutationService,
) {
    @DgsMutation
    fun createPost(input: CreatePostInput): Long {
        return postMutationService.createPost(
            title = input.title,
            startDate = input.start_date,
            endDate = input.end_date,
            ownerUsername = input.owner_username,
        )
    }
}
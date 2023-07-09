package hackathon.sprinter.post.controller

import com.netflix.dgs.codegen.generated.types.PostList
import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsQuery
import hackathon.sprinter.post.service.PostQueryService
import hackathon.sprinter.util.toGqlSchema
import org.springframework.security.access.prepost.PreAuthorize

@DgsComponent
@PreAuthorize("hasAnyRole('USER', 'ADMIN', 'ANONYMOUS')")
class PostQueryResolver(
    private val postQueryService: PostQueryService,
) {
    @DgsQuery
    fun getAllPostList(): PostList {
        val allPost = postQueryService.getAllPostList()

        return PostList(
            allPost.size,
            allPost.map { it.toGqlSchema() }
        )
    }
}
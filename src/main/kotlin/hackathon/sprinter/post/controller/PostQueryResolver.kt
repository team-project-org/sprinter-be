package hackathon.sprinter.post.controller

import com.netflix.dgs.codegen.generated.types.PostList
import com.netflix.graphql.dgs.DgsComponent
import com.netflix.graphql.dgs.DgsQuery
import hackathon.sprinter.post.service.PostQueryService
import hackathon.sprinter.util.toGqlSchema

@DgsComponent
class PostQueryResolver(
    private val postQueryService: PostQueryService,
) {
    @DgsQuery
    fun getAllPostList(): PostList {
        val allPostList = postQueryService.getAllPostList()

        return PostList(
            allPostList.size,
            allPostList.map { it.toGqlSchema() }
        )
    }
}
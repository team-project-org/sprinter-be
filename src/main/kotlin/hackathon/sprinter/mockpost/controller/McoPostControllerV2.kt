package hackathon.sprinter.mockpost.controller

import hackathon.sprinter.mockpost.model.MockPostModel
import hackathon.sprinter.mockpost.service.MockPostCommandService
import hackathon.sprinter.mockpost.service.MockPostQueryService
import io.swagger.annotations.Api
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@Api(tags = ["MockPost"])
@RequestMapping("/api/collections/mockposts")
class McoPostControllerV2(
    private val mockPostQueryService: MockPostQueryService,
    private val mockPostCommandService: MockPostCommandService,
) {
    @GetMapping("/records")
    fun getPostList(
        @RequestParam("page") page: Int,
        @RequestParam("size") size: Int,
    ): List<MockPostModel> {
        return mockPostQueryService.findMockPostListByPaging(page, size)
    }

    @GetMapping("/records/{id}")
    fun getPostById(
        @PathVariable("id") id: String
    ): MockPostModel {
        return mockPostQueryService.findMockPostById(id)
    }

    @PostMapping
    fun createMockPost(
        @RequestParam googleFormCsv: MultipartFile
    ): List<String> {
        return mockPostCommandService.createPostByCsv(googleFormCsv)
    }
}
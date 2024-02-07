package hackathon.sprinter.post.controller

import hackathon.sprinter.post.service.PostMutationService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
class MockPostController(
    private val postMutationService: PostMutationService,
) {
    @PostMapping("/mockPost")
    fun createMockPost(@RequestParam googleFormCsv: MultipartFile) {
        return postMutationService.createPostByCsv(googleFormCsv)
    }

}
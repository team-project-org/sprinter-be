package hackathon.sprinter.mockmember.controller

import hackathon.sprinter.member.creator.MemberCreator
import hackathon.sprinter.mockmember.model.MockMemberDto
import hackathon.sprinter.mockmember.service.MockMemberQueryService
import io.swagger.annotations.Api
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@Api(tags = ["MockMember"])
@RequestMapping("/api/collections/mockmembers")
class MockMemberController(
    private val mockMemberQueryService: MockMemberQueryService,
    private val memberCreator: MemberCreator,
) {

    @GetMapping("/records")
    fun getMemberList(
        @RequestParam("page") page: Int,
        @RequestParam("size") size: Int,
    ): List<MockMemberDto> {
        return mockMemberQueryService.getMockMemberListByPaging(page, size)
    }

    @GetMapping("/records/{id}")
    fun getMemberById(
        @PathVariable("id") id: String,
    ): MockMemberDto {
        return mockMemberQueryService.getMockMemberById(id)
    }

    @PostMapping
    fun createMockMember(@RequestParam googleFormCsv: MultipartFile): List<String> {
        return memberCreator.createMockMemberList(googleFormCsv)
    }
}
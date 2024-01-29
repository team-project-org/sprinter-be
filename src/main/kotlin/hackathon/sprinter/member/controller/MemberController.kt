package hackathon.sprinter.member.controller

import hackathon.sprinter.member.creator.MemberCreator
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
class MemberController(
    private val memberCreator: MemberCreator,
) {

    @PostMapping("/member")
    fun createMockMember(@RequestParam googleFormCsv: MultipartFile): List<Long> {
        return memberCreator.createMockMemberList(googleFormCsv)
    }
}
package hackathon.sprinter.post.service

import hackathon.sprinter.member.service.MemberQueryService
import hackathon.sprinter.mockpost.dto.MockPostInput
import hackathon.sprinter.post.model.MemberPost
import hackathon.sprinter.post.model.MockPost
import hackathon.sprinter.post.model.Post
import hackathon.sprinter.post.repository.MockPostJpaRepository
import hackathon.sprinter.util.CSVReader
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.time.OffsetDateTime

@Service
class PostMutationService(
    private val memberQueryService: MemberQueryService,
    private val mockPostJpaRepository: MockPostJpaRepository
) {
    @Transactional
    fun createPost(
        title: String,
        startDate: OffsetDateTime?,
        endDate: OffsetDateTime?,
        ownerUsername: String
    ): Post {
        val owner = memberQueryService.findMemberByUsername(ownerUsername)
        val post = Post(
            title = title,
            startDate = startDate,
            endDate = endDate,
            memberPostList = mutableListOf(),
        )
        val memberPost = MemberPost(owner, post)

        return owner.createPost(memberPost, post)
    }

    @Transactional
    fun createPostByCsv(csv: MultipartFile) {
        val reader = CSVReader(csv.inputStream.reader())
        val input = reader.readMultiColumn(
            listOf(
                "이메일",
                "프로젝트 명",
                "프로젝트 소개 링크 또는 텍스트",
                "노출될 이미지",
                "카카오톡 오픈채팅 링크"
            )
        ) { MockPostInput(it[0], it[1], it[2], it[3], it[4]) }
        input.map { createMockPost(it)}
    }

    fun createMockPost(input: MockPostInput) {
        val mockPost = MockPost(
            email = input.email,
            projectName = input.projectName,
            link = input.link,
            image = input.image,
            openChatLink = input.openChatLink
        )
        mockPostJpaRepository.save(mockPost)
    }
}
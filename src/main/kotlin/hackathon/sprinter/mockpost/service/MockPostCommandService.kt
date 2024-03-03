package hackathon.sprinter.mockpost.service

import hackathon.sprinter.mockpost.dto.MockPostInput
import hackathon.sprinter.mockpost.repository.MockPostRepositoryV2
import hackathon.sprinter.util.CSVReader
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile

@Service
class MockPostCommandService(
    private val mockPostRepositoryV2: MockPostRepositoryV2,
) {
    @Transactional
    fun createPostByCsv(csv: MultipartFile): List<String> {
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

        return input.map { createMockPost(it) }
    }

    fun createMockPost(input: MockPostInput): String {
        val mockPostInput = MockPostInput(
            email = input.email,
            projectName = input.projectName,
            link = input.link,
            image = input.image,
            openChatLink = input.openChatLink
        )
        return mockPostRepositoryV2.createMockPost(mockPostInput)
    }
}
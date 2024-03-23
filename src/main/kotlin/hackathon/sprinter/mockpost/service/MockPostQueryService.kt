package hackathon.sprinter.mockpost.service

import hackathon.sprinter.mockpost.model.MockPostDto
import hackathon.sprinter.mockpost.repository.MockPostRepositoryV2
import org.springframework.stereotype.Service

@Service
class MockPostQueryService(
    private val mockPostRepositoryV2: MockPostRepositoryV2,
) {
    fun findMockPostListByPaging(page: Int, size: Int): List<MockPostDto> {
        return mockPostRepositoryV2.findMockPostList(page, size)
            .map { dao -> MockPostDto.from(dao) }
    }

    fun findMockPostById(id: String): MockPostDto {
        return mockPostRepositoryV2.findMockPostById(id)
            .let { dao -> MockPostDto.from(dao)}
    }
}
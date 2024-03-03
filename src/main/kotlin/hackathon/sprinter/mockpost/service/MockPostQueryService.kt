package hackathon.sprinter.mockpost.service

import hackathon.sprinter.mockpost.model.MockPostModel
import hackathon.sprinter.mockpost.repository.MockPostRepositoryV2
import org.springframework.stereotype.Service

@Service
class MockPostQueryService(
    private val mockPostRepositoryV2: MockPostRepositoryV2,
) {
    fun findMockPostListByPaging(page: Int, size: Int): List<MockPostModel> {
        return mockPostRepositoryV2.findMockPostList(page, size)
            .map { dao -> MockPostModel.from(dao) }
    }

    fun findMockPostById(id: String): MockPostModel {
        return mockPostRepositoryV2.findMockPostById(id)
            .let { dao -> MockPostModel.from(dao)}
    }
}
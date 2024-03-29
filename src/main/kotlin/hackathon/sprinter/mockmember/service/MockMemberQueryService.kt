package hackathon.sprinter.mockmember.service

import hackathon.sprinter.mockmember.model.MockMemberDto
import hackathon.sprinter.mockmember.repository.MockMemberRepositoryV2
import org.springframework.stereotype.Service

@Service
class MockMemberQueryService(
    private val mockMemberRepositoryV2: MockMemberRepositoryV2,
) {
    fun getMockMemberListByPaging(page: Int, size: Int): List<MockMemberDto> {
        return mockMemberRepositoryV2.findMockMemberList(page, size)
            .map { dao -> MockMemberDto.from(dao) }
    }

    fun getMockMemberById(id: String): MockMemberDto {
        return mockMemberRepositoryV2.findMockMemberById(id)
            .let { dao -> MockMemberDto.from(dao)}
    }
}
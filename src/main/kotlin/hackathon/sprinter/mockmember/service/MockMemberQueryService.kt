package hackathon.sprinter.mockmember.service

import hackathon.sprinter.mockmember.model.MockMember
import hackathon.sprinter.mockmember.repository.MockMemberRepositoryV2
import org.springframework.stereotype.Service

@Service
class MockMemberQueryService(
    private val mockMemberRepositoryV2: MockMemberRepositoryV2,
) {
    fun getMockMemberListByPaging(page: Int, size: Int): List<MockMember> {
        return mockMemberRepositoryV2.findMockMemberList(page, size)
            .map { dao -> MockMember.from(dao) }
    }

    fun getMockMemberById(id: String): MockMember {
        return mockMemberRepositoryV2.findMockMemberById(id)
            .let { dao -> MockMember.from(dao)}
    }
}
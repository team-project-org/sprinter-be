package hackathon.sprinter.mockmember.service

import hackathon.sprinter.mockmember.dto.MockMemberInput
import hackathon.sprinter.mockmember.repository.MockMemberRepositoryV2
import org.springframework.stereotype.Service

@Service
class MockMemberCommandService(
    private val mockMemberRepositoryV2: MockMemberRepositoryV2,
) {
    fun createMockMember(input: MockMemberInput): String {
        return mockMemberRepositoryV2.saveMockMember(input)
    }
}
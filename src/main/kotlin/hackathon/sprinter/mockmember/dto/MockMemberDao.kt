package hackathon.sprinter.mockmember.dto

import hackathon.sprinter.util.MockBaseEntity

data class MockMemberDao(
    val email: String,
    val name: String,
    val job: String,
    val profileImageUrl: String,
    val portfolioLinkPlainText: String,
) : MockBaseEntity()
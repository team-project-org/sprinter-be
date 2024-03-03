package hackathon.sprinter.mockmember.dto

import hackathon.sprinter.util.MockBaseEntity

data class MockMemberDAO(
    val email: String,
    val name: String,
    val job: String,
    val profileImageUrl: String,
    val portfolioLinkPlainText: String,
) : MockBaseEntity()
package hackathon.sprinter.mockpost.dto

import hackathon.sprinter.util.MockBaseEntity

data class MockPostDao(
    val email: String,
    val projectName: String,
    val link: String,
    val image: String,
    val openChatLink: String,
) : MockBaseEntity()
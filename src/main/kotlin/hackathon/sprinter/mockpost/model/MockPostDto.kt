package hackathon.sprinter.mockpost.model

import hackathon.sprinter.mockpost.dto.MockPostDao

class MockPostDto(
    val email: String,
    val projectName: String,
    val link: String,
    val image: String,
    val openChatLink: String,
) {
    companion object {
        fun from(dao: MockPostDao): MockPostDto {
            return MockPostDto(
                email = dao.email,
                projectName = dao.projectName,
                link = dao.link,
                image = dao.image,
                openChatLink = dao.openChatLink,
            )
        }
    }
}

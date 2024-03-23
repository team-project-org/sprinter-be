package hackathon.sprinter.mockmember.model

import hackathon.sprinter.member.model.Member
import hackathon.sprinter.mockmember.dto.MockMemberDao
import hackathon.sprinter.mockmember.dto.MockMemberInput
import hackathon.sprinter.profile.model.Link
import hackathon.sprinter.profile.model.Profile
import hackathon.sprinter.util.AffiliationType
import hackathon.sprinter.util.Job
import hackathon.sprinter.util.JobGroup
import hackathon.sprinter.util.JobLevel

class MockMemberDto(
    val email: String,
    val name: String,
    val job: String,
    val profileImageUrl: String,
    val portfolioLinkPlainText: String?,
) {
    val portfolioLinkList: MutableList<String> = mutableListOf()

    companion object {
        fun from(mockMemberInput: MockMemberInput): MockMemberDto {
            return MockMemberDto(
                email = mockMemberInput.email,
                name = mockMemberInput.name,
                job = mockMemberInput.job,
                profileImageUrl = mockMemberInput.profileImageUrl,
                portfolioLinkPlainText = mockMemberInput.portfolioLinkPlainText,
            )
        }

        fun from(member: Member, linkList: List<Link>): MockMemberDto {
            val email = member.username
            val profileName = member.profileName
            val profile = member.profile

            return MockMemberDto(
                email = email,
                name = profileName,
                job = profile.job.name,
                profileImageUrl = profile.profileImageUrl ?: "",
                portfolioLinkPlainText = null,
            ).also { it.portfolioLinkList.addAll(linkList.map { it.url }) }
        }

        fun from(input: MockMemberDao): MockMemberDto {
            return MockMemberDto(
                email = input.email,
                name = input.name,
                job = input.job,
                profileImageUrl = input.profileImageUrl,
                portfolioLinkPlainText = input.portfolioLinkPlainText,
            )
        }
    }

    fun createRealMemberProfile(): Profile {
        val member = Member(
            username = email,
            password = "",
            token = "",
            profileName = name,
            roles = mutableListOf(),
            isMock = true,
        )

        val linkCollection =
            portfolioLinkPlainText
                ?.split(",")
                ?.map { Link(it) }
                ?: emptyList()

        return Profile(
            member = member,
            jobLevel = JobLevel.IRRELEVANT,
            jobGroup = JobGroup.NONE,
            job = Job.find(job),
            affiliationType = AffiliationType.NONE,
            affiliation = "",
            jobSkills = "",
            introduction = "",
            portfolioLink = "",
            portfolioFileUrl = "",
            profileImageUrl = profileImageUrl,
        ).also { profile ->
            linkCollection.forEach { it.profile = profile }
            profile.linkList.addAll(linkCollection)
        }
    }
}
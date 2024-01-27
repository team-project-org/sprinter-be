package hackathon.sprinter.mockmember.model

import hackathon.sprinter.member.model.Member
import hackathon.sprinter.profile.model.Link
import hackathon.sprinter.profile.model.Profile
import hackathon.sprinter.util.AffiliationType
import hackathon.sprinter.util.Job
import hackathon.sprinter.util.JobGroup
import hackathon.sprinter.util.JobLevel

class MockMember(
    val email: String,
    val name: String,
    val job: String,
    val profileImageUrl: String,
    val portfolioLinkPlainText: String?,
) {
    val portfolioLinkList: MutableList<String> = mutableListOf()

    companion object {
        fun from(member: Member, linkList: List<Link>): MockMember {
            val email = member.username
            val profileName = member.profileName
            val profile = member.profile

            return MockMember(
                email = email,
                name = profileName,
                job = profile.job.name,
                profileImageUrl = profile.profileImageUrl ?: "",
                portfolioLinkPlainText = null,
            ).also { it.portfolioLinkList.addAll(linkList.map { it.url }) }
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
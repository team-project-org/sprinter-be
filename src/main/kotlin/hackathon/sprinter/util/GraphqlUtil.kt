package hackathon.sprinter.util

import com.netflix.dgs.codegen.generated.types.*
import hackathon.sprinter.member.model.Member
import hackathon.sprinter.post.model.MockPost
import hackathon.sprinter.post.model.Post
import hackathon.sprinter.profile.model.OtherExperience
import hackathon.sprinter.profile.model.Profile
import hackathon.sprinter.profile.model.ProjectExperience
import hackathon.sprinter.profile.model.WorkExperience

fun Post.toGqlSchema(): PostResponse {
    return PostResponse(
        id = this.id.toString(),
        title = this.title,
        start_date = this.startDate,
        end_date = this.endDate,
        owner = this.ownerMember?.toGqlSchema()
    )
}

fun MockPost.toGqlSchema(): MockPostResponse {
    return MockPostResponse(
        email = this.email,
        project_name = this.projectName,
        link =  this.link,
        image = this.image,
        open_chat_link =  this.openChatLink
    )
}

fun Member.toGqlSchema(): MemberResponse {
    return MemberResponse(
        id = this.id.toString(),
        username = this.username,
        profile_name = this.profileName,
        role_type_list = this.roles.map { it.roleType },
    )
}

fun Profile.toGqlSchema(): ProfileResponse {
    return ProfileResponse(
        id = this.id.toString(),
        member_id = this.member?.id.toString(),
        job_group = this.jobGroup,
        job = this.job,
        job_level = this.jobLevel,
        job_skills = this.jobSkills.split(", ").map { JobSkill.find(it) },
        affiliation_type = this.affiliationType,
        affiliation = this.affiliation,
        work_experiences = this.workExperiences.map { it.toGqlSchema() },
        project_experiences = this.projectExperiences.map { it.toGqlSchema() },
        other_experiences = this.otherExperiences.map { it.toGqlSchema() },
        introduction = this.introduction,
        portfolio_link = this.portfolioLink,
        portfolio_file_url = this.portfolioFileUrl,
        profile_image_url = this.profileImageUrl,
    )
}

fun WorkExperience.toGqlSchema(): WorkExperienceResponse {
    return WorkExperienceResponse(
        job_group = this.jobGroup,
        job = this.job,
        company = this.company,
        start_date = convertLocalDateToOffsetDateTime(this.startDate),
        end_date = convertLocalDateToOffsetDateTime(this.endDate),
    )
}

fun ProjectExperience.toGqlSchema(): ProjectExperienceResponse {
    return ProjectExperienceResponse(
        job_group = this.jobGroup,
        job = this.job,
        project_name = this.projectName,
        start_date = convertLocalDateToOffsetDateTime(this.startDate),
        end_date = convertLocalDateToOffsetDateTime(this.endDate),
    )
}

fun OtherExperience.toGqlSchema(): OtherExperienceResponse {
    return OtherExperienceResponse(
        activity_name = this.activityName,
        role = this.role,
        start_date = convertLocalDateToOffsetDateTime(this.startDate),
        end_date = convertLocalDateToOffsetDateTime(this.endDate),
    )
}

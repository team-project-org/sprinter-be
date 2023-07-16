package hackathon.sprinter.profile.service

import com.netflix.dgs.codegen.generated.types.CreateProfileInput
import com.netflix.dgs.codegen.generated.types.OtherExperienceInput
import com.netflix.dgs.codegen.generated.types.ProjectExperienceInput
import com.netflix.dgs.codegen.generated.types.WorkExperienceInput
import hackathon.sprinter.profile.model.OtherExperience
import hackathon.sprinter.profile.model.Profile
import hackathon.sprinter.profile.model.ProjectExperience
import hackathon.sprinter.profile.model.WorkExperience
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
class ProfileCreator {
    @Transactional
    fun create(input: CreateProfileInput): Profile {
        return input.toProfile()
    }
}

fun CreateProfileInput.toProfile(): Profile {
    return Profile(
        member = null,
        jobGroup = job_group,
        jobLevel = job_level,
        job = job,
        jobSkills = job_skills.joinToString { it.name },
        affiliationType = affiliation_type,
        affiliation = affiliation,
        introduction = introduction,
        portfolioLink = portfolio_link,
        portfolioFileUrl = portfolio_file_url,
        profileImageUrl = profile_image_url,
    ).also { profile ->
        profile.workExperiences.addAll(this.work_experiences.map { it.toWorkExperience(profile) })
        profile.projectExperiences.addAll(this.project_experiences.map { it.toProjectExperience(profile) })
        profile.otherExperiences.addAll(this.other_experiences.map { it.toOtherExperience(profile) })
    }
}

fun WorkExperienceInput.toWorkExperience(profile: Profile): WorkExperience {
    return WorkExperience(
        jobGroup = this.job_group,
        job = this.job,
        company = this.company,
        startDate = this.start_date.toLocalDate(),
        endDate = this.end_date.toLocalDate(),
        profile = profile,
    )
}

fun ProjectExperienceInput.toProjectExperience(profile: Profile): ProjectExperience {
    return ProjectExperience(
        jobGroup = this.job_group,
        job = this.job,
        projectName = this.project_name,
        startDate = this.start_date.toLocalDate(),
        endDate = this.end_date.toLocalDate(),
        profile = profile,
    )
}

fun OtherExperienceInput.toOtherExperience(profile: Profile): OtherExperience {
    return OtherExperience(
        activityName = this.activity_name,
        role = this.role,
        startDate = this.start_date.toLocalDate(),
        endDate = this.end_date.toLocalDate(),
        profile = profile,
    )
}
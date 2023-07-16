package hackathon.sprinter.profile.repository

import hackathon.sprinter.profile.model.ProjectExperience
import org.springframework.data.jpa.repository.JpaRepository

interface ProjectExperienceJpaRepository : JpaRepository<ProjectExperience, Long>
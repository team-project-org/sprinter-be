package hackathon.sprinter.profile.repository

import hackathon.sprinter.profile.model.WorkExperience
import org.springframework.data.jpa.repository.JpaRepository

interface WorkExperienceJpaRepository : JpaRepository<WorkExperience, Long>
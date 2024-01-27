package hackathon.sprinter.profile.model

import hackathon.sprinter.configure.dto.BaseTimeEntity
import hackathon.sprinter.member.model.Member
import hackathon.sprinter.util.AffiliationType
import hackathon.sprinter.util.Job
import hackathon.sprinter.util.JobGroup
import hackathon.sprinter.util.JobLevel
import java.time.LocalDate
import javax.persistence.CascadeType
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.FetchType
import javax.persistence.GeneratedValue
import javax.persistence.GenerationType
import javax.persistence.Id
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.OneToMany
import javax.persistence.OneToOne
import javax.persistence.Table

@Entity
@Table(name = "profiles")
class Profile(
    @OneToOne(fetch = FetchType.LAZY) @JoinColumn(name = "member_id") var member: Member?,
    @Column(nullable = false) @Enumerated(EnumType.STRING) val jobLevel: JobLevel,
    @Column(nullable = false) @Enumerated(EnumType.STRING) val jobGroup: JobGroup,
    @Column(nullable = false) @Enumerated(EnumType.STRING) val job: Job,
    @Column(nullable = false) @Enumerated(EnumType.STRING) val affiliationType: AffiliationType,
    @Column(nullable = false) val affiliation: String,
    @Column(nullable = false) val jobSkills: String,
    @Column(nullable = false) val introduction: String,
    @Column(nullable = false) val portfolioLink: String?,
    @Column(nullable = false) val portfolioFileUrl: String?,
    @Column(nullable = false) val profileImageUrl: String?,
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long = 0L,
) : BaseTimeEntity() {
    @OneToMany(cascade = [CascadeType.ALL]) @JoinColumn(name = "profile_id")
    val workExperiences: MutableList<WorkExperience> = mutableListOf()

    @OneToMany(cascade = [CascadeType.ALL]) @JoinColumn(name = "profile_id")
    val projectExperiences: MutableList<ProjectExperience> = mutableListOf()

    @OneToMany(cascade = [CascadeType.ALL]) @JoinColumn(name = "profile_id")
    val otherExperiences: MutableList<OtherExperience> = mutableListOf()

    @OneToMany(cascade = [CascadeType.ALL]) @JoinColumn(name = "profile_id")
    val linkList: MutableList<Link> = mutableListOf()
}

@Entity
@Table(name ="work_experiences")
class WorkExperience(
    @Column(nullable = false) @Enumerated(EnumType.STRING) val jobGroup: JobGroup,
    @Column(nullable = false) @Enumerated(EnumType.STRING) val job: Job,
    @Column val company: String,
    @Column val startDate: LocalDate,
    @Column val endDate: LocalDate,
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "profile_id")
    val profile: Profile,
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long = 0L,
) : BaseTimeEntity()

@Entity
@Table(name ="project_experiences")
class ProjectExperience(
    @Column(nullable = false) @Enumerated(EnumType.STRING) val jobGroup: JobGroup,
    @Column(nullable = false) @Enumerated(EnumType.STRING) val job: Job,
    @Column val projectName: String,
    @Column val startDate: LocalDate,
    @Column val endDate: LocalDate,
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "profile_id")
    val profile: Profile,
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long = 0L,
) : BaseTimeEntity()

@Entity
@Table(name ="other_experiences")
class OtherExperience(
    @Column val activityName: String,
    @Column val startDate: LocalDate,
    @Column val endDate: LocalDate,
    @Column val role: String,
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "profile_id")
    val profile: Profile,
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long = 0,
) : BaseTimeEntity()

@Entity
@Table(name = "links")
class Link(
    @Column val url: String,
    @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "profile_id") var profile: Profile?,
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long = 0L,
) : BaseTimeEntity() {
    constructor(url: String) : this(
        url.trim(),
        null
    )
}

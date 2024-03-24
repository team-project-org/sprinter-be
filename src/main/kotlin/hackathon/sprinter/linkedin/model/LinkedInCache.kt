package hackathon.sprinter.linkedin.model

import hackathon.sprinter.configure.dto.BaseTimeEntity
import javax.persistence.*

@Entity
@Table(name = "linkedIn_cache")
class LinkedInCache(
    @Column(nullable = false) val profileUrl: String,
    @Column(columnDefinition = "LONGTEXT") var html: String,
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long = 0L,
) : BaseTimeEntity() {

    fun updateHtml(html: String) {
        this.html = html
    }

}
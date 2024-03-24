package hackathon.sprinter.github.model

import hackathon.sprinter.configure.dto.BaseTimeEntity
import javax.persistence.*

@Entity
@Table(name = "github_cache")
class GithubCache(
    @Column(nullable = false) val username: String,
    @Column(nullable = false, columnDefinition = "LONGTEXT") var html: String,
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long = 0L,
) : BaseTimeEntity() {
    fun updateHtml(html: String) {
        this.html = html
    }

}
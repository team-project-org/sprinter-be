package hackathon.sprinter.post.model

import hackathon.sprinter.configure.dto.BaseTimeEntity
import javax.persistence.*

@Entity
@Table(name = "mock_posts")
class MockPost(
    @Column(nullable = false) val email: String,
    @Column(nullable = false) val projectName: String,
    @Column(nullable = false) val link: String,
    @Column(nullable = false) val image: String,
    @Column(nullable = false) val openChatLink: String,
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) val id: Long = 0L
) : BaseTimeEntity() {

}
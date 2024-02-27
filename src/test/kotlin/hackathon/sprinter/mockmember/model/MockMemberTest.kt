package hackathon.sprinter.mockmember.model

import hackathon.sprinter.TestDatabaseSupport
import hackathon.sprinter.member.repository.MemberJpaRepository
import hackathon.sprinter.profile.repository.LinkJpaRepository
import hackathon.sprinter.profile.repository.ProfileJpaRepository
import hackathon.sprinter.util.Job.DEVELOPER
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import org.junit.jupiter.api.Test

class MockMemberTest(
    private val memberJpaRepository: MemberJpaRepository,
    private val profileJpaRepository: ProfileJpaRepository,
    private val linkJpaRepository: LinkJpaRepository,
) : TestDatabaseSupport() {

    @Test
    fun `인재 Mock form 정보를 받아서 프로필 엔티티를 생성한다`() {
        // given
        val mockMember = MockMember(
            email = "test@test.com",
            name = "테스트계정",
            job = "개발자",
            profileImageUrl = "testProfileImage",
            portfolioLinkPlainText = "https://ws-pace.tistory.com/, https://github.com/choiwoonsik"
        )

        // when
        val profile = mockMember.createRealMemberProfile()

        // then
        profile.member shouldNotBe null
        profile.linkList.first().url shouldBe "https://ws-pace.tistory.com/"
        val member = profile.member!!
        member.username shouldBe "test@test.com"
        member.profileName shouldBe "테스트계정"
        member.password shouldBe ""
        member.isMock shouldBe true
        profile.job shouldBe DEVELOPER
    }

    @Test
    fun `인재 Mock form 정보로 생성한 프로필 엔티티를 저장한다`() {
        // given
        val mockMember = MockMember(
            email = "test@test.com",
            name = "테스트계정",
            job = "개발자",
            profileImageUrl = "testProfileImage",
            portfolioLinkPlainText = "https://ws-pace.tistory.com/, https://github.com/choiwoonsik"
        )

        // when
        val profile = mockMember.createRealMemberProfile()
        val member = profile.member!!
        val linkList = profile.linkList

        memberJpaRepository.save(member)
        profileJpaRepository.save(profile)
        linkJpaRepository.saveAll(linkList)

        // then
        val memberEntity = memberJpaRepository.findAll().first { it.profileName == "테스트계정" }
        memberEntity.username shouldBe mockMember.email
        memberEntity.isMock shouldBe true

        val profileEntity = profileJpaRepository.findAll().first { it.member!!.id == memberEntity.id }
        profileEntity.job shouldBe DEVELOPER
        profileEntity.linkList.size shouldBe 2
        profileEntity.introduction.isBlank() shouldBe true

        val links = linkJpaRepository.findAll().filter { it.profile?.id == profileEntity.id }
        links.size shouldBe 2
        links[0].url shouldBe "https://ws-pace.tistory.com/"
        links[1].url shouldBe "https://github.com/choiwoonsik"
    }
}
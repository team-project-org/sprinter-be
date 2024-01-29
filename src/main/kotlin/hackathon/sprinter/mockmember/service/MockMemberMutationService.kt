package hackathon.sprinter.mockmember.service

import com.netflix.dgs.codegen.generated.types.MockMemberInput
import hackathon.sprinter.configure.DataNotFoundException
import hackathon.sprinter.configure.dto.ErrorCode
import hackathon.sprinter.member.repository.MemberRepository
import hackathon.sprinter.mockmember.model.MockMember
import hackathon.sprinter.profile.repository.LinkJpaRepository
import hackathon.sprinter.profile.repository.ProfileJpaRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class MockMemberMutationService(
    private val memberRepository: MemberRepository,
    private val profileJpaRepository: ProfileJpaRepository,
    private val linkJpaRepository: LinkJpaRepository,
) {
    @Transactional
    fun createMockMember(input: MockMemberInput): Long {
        return MockMember(
            email = input.email,
            name = input.name,
            job = input.job,
            profileImageUrl = input.profile_image_url,
            portfolioLinkPlainText = input.portfolio_link_plain_text,
        )
            .createRealMemberProfile()
            .also {
                if (it.member == null) throw DataNotFoundException(ErrorCode.MEMBER_NOT_EXIST, "회원이 존재하지 않습니다.")
                memberRepository.save(it.member!!)
                profileJpaRepository.save(it)
                linkJpaRepository.saveAll(it.linkList)
            }
            .let { it.member!!.id }
    }
}
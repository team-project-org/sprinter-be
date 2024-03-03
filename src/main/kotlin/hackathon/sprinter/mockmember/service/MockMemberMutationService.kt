package hackathon.sprinter.mockmember.service

import hackathon.sprinter.configure.DataNotFoundException
import hackathon.sprinter.configure.dto.ErrorCode
import hackathon.sprinter.member.repository.MemberRepository
import hackathon.sprinter.mockmember.dto.MockMemberInput
import hackathon.sprinter.mockmember.model.MockMemberDto
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
        return MockMemberDto
            .from(input)
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
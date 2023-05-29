package hackathon.sprinter.member.service

import com.netflix.dgs.codegen.generated.types.MemberResponse
import com.netflix.dgs.codegen.generated.types.SignupInput
import com.netflix.dgs.codegen.generated.types.UpdateProfileNameInput
import hackathon.sprinter.configure.DataNotFoundException
import hackathon.sprinter.configure.ParameterInvalidException
import hackathon.sprinter.configure.dto.ErrorCode
import hackathon.sprinter.configure.dto.ErrorCode.ITEM_NOT_EXIST
import hackathon.sprinter.member.creator.MemberCreator
import hackathon.sprinter.member.model.Member
import hackathon.sprinter.member.model.RoleType
import hackathon.sprinter.member.repository.MemberRepository
import hackathon.sprinter.util.toGqlSchema
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.regex.Pattern

@Service
class MemberService(
    private val memberRepository: MemberRepository,
    private val memberCreator: MemberCreator,
) {
    @Transactional(readOnly = true)
    fun findMemberById(memberId: Long): MemberResponse {
        val member = memberRepository.findMemberById(memberId)
            ?: throw DataNotFoundException(ITEM_NOT_EXIST, "회원이 존재하지 않습니다.")

        return member.toGqlSchema()
    }

    @Transactional(readOnly = true)
    fun findAllMember(): List<MemberResponse> {
        val memberList = memberRepository.findAllMember()
        if (memberList.isEmpty()) {
            return emptyList()
        }
        return memberList.map { it.toGqlSchema() }
    }

    @Transactional(readOnly = true)
    fun findMemberByToken(token: String): Member {
        return memberRepository.findMemberByToken(token)
            ?: throw DataNotFoundException(ITEM_NOT_EXIST, "회원이 존재하지 않습니다.")
    }

    @Transactional(readOnly = true)
    fun findMemberByUsername(username: String): Member {
        return memberRepository.findMemberByUsername(username)
            ?: throw DataNotFoundException(ITEM_NOT_EXIST, "회원이 존재하지 않습니다.")
    }

    @Transactional
    fun create(input: SignupInput): Long {
        validate(input)
        val member = doCreate(input)
        return member.id
    }

    private fun validate(input: SignupInput) {
        input
            .apply { checkDuplicateUsername(this) }
            .apply { checkPassword(this) }
    }

    private fun checkDuplicateUsername(input: SignupInput): Boolean {
        try {
            check(memberRepository.findMemberByUsername(input.username) == null)
            return true
        } catch (e: Exception) {
            throw ParameterInvalidException(
                ErrorCode.PARAMETER_INVALID, "동일한 계정이 존재합니다."
            )
        }
    }

    private fun checkPassword(input: SignupInput): Boolean {
        val password = input.password
        val pattern = Pattern.compile("^(?=.*[a-zA-Z])(?=.*\\d)(?=.*\\W).{8,20}\$")
        return try {
            check(pattern.matcher(password).matches())
            true
        } catch (e: Exception) {
            throw ParameterInvalidException(
                ErrorCode.PARAMETER_INVALID, "패스워드는 영문자와 특수문자를 포함하여 8자 이상 20자 이하로 이뤄져야 합니다."
            )
        }
    }

    private fun doCreate(input: SignupInput): Member {
        val member = memberCreator.createMember(input)
        val userRole = RoleType.ROLE_USER

        member.addRoleType(userRole)
        return memberRepository.save(member)
    }

    @Transactional
    fun updateProfileName(input: UpdateProfileNameInput): Long {
        val member = memberRepository
            .findMemberById(input.id.toLong())
            ?: throw DataNotFoundException(ITEM_NOT_EXIST, "회원이 존재하지 않습니다.")

        member.profileName = input.new_profile_name
        return member.id
    }
}


@Service
class MemberAssembler(
    private val memberRepository: MemberRepository
) {
    @Transactional
    fun assemble(memberId: Long): MemberResponse {
        val member = memberRepository.findMemberById(memberId)
            ?: throw DataNotFoundException(ITEM_NOT_EXIST, "회원이 존재하지 않습니다.")

        return MemberResponse(
            id = member.id.toString(),
            username = member.username,
            profile_name = member.profileName,
            role_type_list = member.roleList.map { it.roleType }
        )
    }
}

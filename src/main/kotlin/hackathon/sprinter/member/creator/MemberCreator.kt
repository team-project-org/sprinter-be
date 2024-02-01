package hackathon.sprinter.member.creator

import com.netflix.dgs.codegen.generated.types.SignupInput
import com.netflix.dgs.codegen.generated.types.UpdateProfileNameInput
import hackathon.sprinter.configure.DataNotFoundException
import hackathon.sprinter.configure.ParameterInvalidException
import hackathon.sprinter.configure.dto.ErrorCode
import hackathon.sprinter.member.dto.MockMemberInput
import hackathon.sprinter.member.model.Member
import hackathon.sprinter.member.repository.MemberRepository
import hackathon.sprinter.mockmember.service.MockMemberMutationService
import hackathon.sprinter.util.CSVReader
import hackathon.sprinter.util.RoleType
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.util.regex.Pattern

@Component
class MemberCreator(
    private val passwordEncoder: BCryptPasswordEncoder,
    private val memberRepository: MemberRepository,
    private val mockMemberMutationService: MockMemberMutationService,
) {
    @Transactional
    fun updateProfileName(input: UpdateProfileNameInput): Long {
        val member = memberRepository
            .findMemberById(input.id.toLong())
            ?: throw DataNotFoundException(ErrorCode.ITEM_NOT_EXIST, "회원이 존재하지 않습니다.")

        member.profileName = input.new_profile_name
        return member.id
    }

    @Transactional
    fun create(input: SignupInput): Long {
        validate(input)
        val member = doCreate(input)
        return member.id
    }

    @Transactional
    fun createMockMemberList(csv: MultipartFile): List<Long> {
        val reader = CSVReader(csv.inputStream.reader())
        val input = reader
            .readMultiColumn(
                listOf(
                    "이메일",
                    "프로필에 사용할 이름",
                    "직무분야",
                    "프로필 이미지",
                    "소개할 수 있는 링크"
                )
            ) { MockMemberInput(it[0], it[1], it[2], it[3], it[4]) }

        return input.map { createMockMember(it) }
    }

    fun createMockMember(input: MockMemberInput): Long {
        validateMock(input)

        return mockMemberMutationService.createMockMember(input)
    }

    private fun validateMock(input: MockMemberInput) {
        val emailRegex = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}".toRegex()
        check(input.email.isNotBlank() && emailRegex.matches(input.email)) { "이메일 형식이 올바르지 않습니다." }

        val nameRegex = "^[가-힣]{2,4}\$".toRegex()
        check(input.name.isNotBlank()) { "이름은 필수 입력값입니다." }
        check(nameRegex.matches(input.name)) { "이름은 공백없이 한글로 2~4 글자만 입력 가능합니다." }
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
        val member = createMember(input)
        member.addRoleType(RoleType.ROLE_USER)
        return memberRepository.save(member)
    }

    private fun createMember(input: SignupInput): Member {
        val encodePassword = passwordEncoder.encode(input.password)
        return Member(
            username = input.username,
            password = encodePassword,
            token = "",
            profileName = input.profile_name,
            roles = mutableListOf()
        )
    }
}

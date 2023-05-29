package hackathon.sprinter.member.creator

import com.netflix.dgs.codegen.generated.types.CreateMemberInput
import com.netflix.dgs.codegen.generated.types.SignupInput
import hackathon.sprinter.member.model.Member
import hackathon.sprinter.member.model.Role
import hackathon.sprinter.member.model.RoleType
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Component

@Component
class MemberCreator(
    private val passwordEncoder: BCryptPasswordEncoder,
) {
    fun createMember(input: SignupInput): Member {
        val encodePassword = passwordEncoder.encode(input.password)
        return Member(
            username = input.username,
            password = encodePassword,
            token = "",
            profileName = input.profile_name,
            roleList = mutableListOf(Role(RoleType.ROLE_USER))
        )
    }
}

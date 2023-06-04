package hackathon.sprinter.member.creator

import com.netflix.dgs.codegen.generated.types.SignupInput
import hackathon.sprinter.member.model.Member
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
            roleList = mutableListOf()
        )
    }
}

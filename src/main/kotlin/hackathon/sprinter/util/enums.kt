package hackathon.sprinter.util

import hackathon.sprinter.configure.DataNotFoundException
import hackathon.sprinter.configure.dto.ErrorCode
import org.springframework.security.access.AuthorizationServiceException
import javax.persistence.AttributeConverter
import javax.persistence.Converter

enum class RoleType {
    ROLE_USER, ROLE_ADMIN;

    companion object {
        fun find(roleName: String): RoleType {
            return RoleType.values().firstOrNull { it.name == roleName }
                ?: throw AuthorizationServiceException("해당하는 권한이 없습니다.")
        }
    }
}

@Converter(autoApply = true)
class RoleTypeEnumConverter : AttributeConverter<RoleType, String> {
    override fun convertToEntityAttribute(dbCode: String): RoleType {
        return RoleType.find(dbCode)
    }

    override fun convertToDatabaseColumn(type: RoleType?): String? {
        return type?.name
    }
}

enum class AffiliationType {
    COMPANY, UNIVERSITY, HIGH_SCHOOL, NONE
}

enum class JobLevel(level: String) {
    IRRELEVANT("경력 무관"),
    INTERN("인턴"),
    BEGINNER("신입"),
    JUNIOR("1~3년"),
    MIDDLE("4~8년"),
    SENIOR("9년 이상"),
    TOP("리더"),
}

enum class JobGroup(group: String) {
    DEVELOPER("개발"),
    GAME_DEVELOPER("게임개발"),
    DESIGN("디자인"),
    PRODUCT("기획"),
}

enum class Job(name: String) {
    BACKEND_DEVELOPER("벡엔드/서버 개발자"),
    FRONTEND_DEVELOPER("프론트엔드/웹퍼블리셔 개발자"),
    ANDROID_DEVELOPER("안드로이드 개발자"),
    IOS_DEVELOPER("IOS 개발자"),

    GAME_SERVER_DEVELOPER("게임 서버 개발자"),
    GAME_CLIENT_DEVELOPER("게임 클라이언트 개발자"),
    GAME_PRODUCT_OWNER("게임 기획자"),

    PRODUCT_DESIGNER("프로덕트 디자이너"),
    WEB_APP_DESIGNER("웹/앱 디자이너"),
    GRAPHICS_DESIGNER("그래픽 디자이너"),

    SERVICE_OWNER("서비스 기획자"),
    PO_PM("PO/PM"),
    BUSINESS_DEVELOPMENT_PLANNER("사업개발 기획자"),
}

enum class JobSkill(name: String) {
    Photoshop("Photoshop"),
    MSOffice("MS-Office"),
    GoogleAnalytics("Google Analytics"),
    Illustrator("Illustrator"),
    Python("Python"),
    AWS("AWS"),
    JavaScript("JavaScript"),
    React("React"),
    Java("Java"),
    TypeScript("TypeScript"),
    Figma("Figma"),
    MySQL("MySQL"),
    NodeJs("Node.js"),
    Git("Git"),
    HtmlCss("HTML/CSS"),
    SQL("SQL"),
    Kotlin("Kotlin"),
    Github("Github"),
    Docker("Docker"),
    Spring("Spring"),
    ReactNative("React Native"),
    ;

    companion object {
        fun find(name: String): JobSkill {
            return JobSkill.values().firstOrNull { it.name == name } ?: throw DataNotFoundException(
                ErrorCode.ITEM_NOT_EXIST,
                "존재하지 않는 직무 기술입니다."
            )
        }
    }
}

enum class TagType {
    JOB_SKILL_KEYWORD
}
package hackathon.sprinter.mockmember.repository

import com.google.gson.Gson
import com.google.gson.JsonObject
import hackathon.sprinter.mockmember.dto.MockMemberDAO
import hackathon.sprinter.mockmember.dto.MockMemberInput
import hackathon.sprinter.util.RestTemplateService
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Repository

@Repository
class MockMemberRepositoryV2(
    private val restTemplateService: RestTemplateService,
    private val gson: Gson,
) {

    fun findMockMemberList(
        page: Int,
        size: Int,
    ): List<MockMemberDAO> {
        return restTemplateService.fetch(
            HttpMethod.GET,
            "/api/collections/mockmembers/records?page=$page&perPage=$size",
            JsonObject::class.java,
            null,
            "",
        ).get("items").asJsonArray
            .let { it.map { gson.fromJson(it, MockMemberDAO::class.java) } }
    }

    fun findMockMemberById(id: String): MockMemberDAO {
        return restTemplateService.fetch(
            HttpMethod.GET,
            "/api/collections/mockmembers/records?id=$id",
            JsonObject::class.java,
            null,
            "",
        ).get("items").asJsonArray.firstOrNull()
            .let { it ?: throw NoSuchElementException("No such element with id: $id") }
            .let { gson.fromJson(it, MockMemberDAO::class.java) }
    }

    fun saveMockMember(mockMemberInput: MockMemberInput): String {
        return restTemplateService.fetch(
            HttpMethod.POST,
            "/api/collections/mockmembers/records",
            JsonObject::class.java,
            null,
            gson.toJson(mockMemberInput),
        ).get("id").asString
    }
}
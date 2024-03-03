package hackathon.sprinter.mockpost.repository

import com.google.gson.Gson
import com.google.gson.JsonObject
import hackathon.sprinter.mockpost.dto.MockPostDao
import hackathon.sprinter.mockpost.dto.MockPostInput
import hackathon.sprinter.util.RestTemplateService
import org.springframework.http.HttpMethod
import org.springframework.stereotype.Repository

@Repository
class MockPostRepositoryV2(
    private val restTemplateService: RestTemplateService,
    private val gson: Gson,
) {
    fun findMockPostList(page: Int, size: Int): List<MockPostDao> {
        return restTemplateService.fetch(
            HttpMethod.GET,
            "/api/collections/mockposts/records?page=$page&perPage=$size",
            JsonObject::class.java,
            null,
            "",
        ).get("items").asJsonArray
            .let { it.map { gson.fromJson(it, MockPostDao::class.java) } }
    }

    fun findMockPostById(id: String): MockPostDao {
        return restTemplateService.fetch(
            HttpMethod.GET,
            "/api/collections/mockposts/records?id=$id",
            JsonObject::class.java,
            null,
            "",
        ).get("items").asJsonArray.firstOrNull()
            .let { it ?: throw NoSuchElementException("No such element with id: $id") }
            .let { gson.fromJson(it, MockPostDao::class.java) }
    }

    fun createMockPost(mockPostInput: MockPostInput): String {
        return restTemplateService.fetch(
            HttpMethod.POST,
            "/api/collections/mockposts/records",
            JsonObject::class.java,
            null,
            gson.toJson(mockPostInput),
        ).get("id").asString
    }
}
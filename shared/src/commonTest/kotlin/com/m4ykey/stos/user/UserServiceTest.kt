package com.m4ykey.stos.user

import com.m4ykey.stos.core.Filters.QUESTION_HOME_FILTER
import com.m4ykey.stos.core.Filters.USER_ANSWER_FILTER
import com.m4ykey.stos.core.Filters.USER_FILTER
import com.m4ykey.stos.question.domain.model.QuestionSort
import com.m4ykey.stos.user.data.remote.service.UserService
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headers
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class UserServiceTest {

    private val responseJson = """
        {
            "items": [],
            "has_more": false
        }
    """.trimIndent()

    private fun createClient(
        engine : MockEngine
    ) : HttpClient {
        return HttpClient(engine) {
            install(ContentNegotiation) {
                json(
                    Json {
                        ignoreUnknownKeys = true
                    }
                )
            }
        }
    }

    private fun jsonHeaders() = headersOf(
        HttpHeaders.ContentType,
        "application/json"
    )

    @Test
    fun `getUserById sends correct endpoint and parameters`() = runTest {
        var path = ""
        var site : String? = null
        var filter : String? = null

        val engine = MockEngine { request ->
            path = request.url.encodedPath
            site = request.url.parameters["site"]
            filter = request.url.parameters["filter"]

            respond(
                content = responseJson,
                status = HttpStatusCode.OK,
                headers = jsonHeaders()
            )
        }

        val client = createClient(engine)

        client.use { client ->
            val service = UserService(client)

            val result = service.getUserById(
                id = 123,
                site = "stackoverflow",
                filter = "user-filter"
            )

            assertEquals("/users/123", path)
            assertEquals("stackoverflow", site)
            assertEquals("user-filter", filter)
            assertEquals(0, result.items.size)
            assertEquals(false, result.has_more)
        }
    }

    @Test
    fun `getUserQuestions sends correct endpoint and parameters`() = runTest {
        var path = ""
        var parameters = emptyMap<String, String>()

        val engine = MockEngine { request ->
            path = request.url.encodedPath
            parameters = request.url.parameters
                .names()
                .associateWith { name ->
                    request.url.parameters[name].orEmpty()
                }

            respond(
                status = HttpStatusCode.OK,
                headers = jsonHeaders(),
                content = responseJson
            )
        }

        val client = createClient(engine)

        client.use { client ->
            val service = UserService(client)

            val result = service.getUserQuestions(
                id = 123,
                site = "stackoverflow",
                filter = "question-filter",
                order = "desc",
                sort = "activity",
                page = 2,
                pageSize = 20
            )

            assertEquals("/users/123/questions", path)
            assertEquals("stackoverflow", parameters["site"])
            assertEquals("desc", parameters["order"])
            assertEquals("activity", parameters["sort"])
            assertEquals("2", parameters["page"])
            assertEquals("20", parameters["page_size"])
            assertEquals("question-filter", parameters["filter"])
            assertEquals(0, result.items.size)
        }
    }

    @Test
    fun `getUserAnswers sends correct endpoint and parameters`() = runTest {
        var path = ""
        var parameters = emptyMap<String, String>()

        val engine = MockEngine { request ->
            path = request.url.encodedPath
            parameters = request.url.parameters
                .names()
                .associateWith { name ->
                    request.url.parameters[name].orEmpty()
                }

            respond(
                status = HttpStatusCode.OK,
                headers = jsonHeaders(),
                content = responseJson
            )
        }

        val client = createClient(engine)

        client.use { client ->
            val service = UserService(client)

            val result = service.getUserAnswers(
                id = 456,
                site = "stackoverflow",
                filter = "answer-filter",
                order = "desc",
                sort = "activity",
                page = 2,
                pageSize = 20
            )

            assertEquals("/users/456/answers", path)
            assertEquals("stackoverflow", parameters["site"])
            assertEquals("desc", parameters["order"])
            assertEquals("activity", parameters["sort"])
            assertEquals("2", parameters["page"])
            assertEquals("20", parameters["page_size"])
            assertEquals("answer-filter", parameters["filter"])
            assertEquals(0, result.items.size)
        }
    }

    @Test
    fun `getUserById uses default parameters`() = runTest {
        var site : String? = null
        var filter : String? = null

        val engine = MockEngine { request ->
            site = request.url.parameters["site"]
            filter = request.url.parameters["filter"]

            respond(
                content = responseJson,
                status = HttpStatusCode.OK,
                headers = jsonHeaders()
            )
        }

        val client = createClient(engine)

        client.use { client ->
            val service = UserService(client)

            service.getUserById(id = 123)

            assertEquals("stackoverflow", site)
            assertNotNull(filter)
            assertNotNull(USER_FILTER, filter)
        }
    }

    @Test
    fun `getUserQuestions uses default parameters`() = runTest {
        var parameters = emptyMap<String, String>()

        val engine = MockEngine { request ->
            parameters = request.url.parameters
                .names()
                .associateWith { name ->
                    request.url.parameters[name].orEmpty()
                }

            respond(
                status = HttpStatusCode.OK,
                content = responseJson,
                headers = jsonHeaders()
            )
        }

        val client = createClient(engine)

        client.use { client ->
            val service = UserService(client)

            service.getUserQuestions(
                page = 1,
                pageSize = 20,
                id = 123
            )

            assertEquals("stackoverflow", parameters["site"])
            assertEquals("desc", parameters["order"])
            assertEquals(
                QuestionSort.ACTIVITY.name,
                parameters["sort"]
            )
            assertEquals("1", parameters["page"])
            assertEquals("20", parameters["page_size"])
            assertEquals(QUESTION_HOME_FILTER, parameters["filter"])
        }
    }

    @Test
    fun `getUserAnswers uses default parameters`() = runTest {
        var parameters = emptyMap<String, String>()

        val engine = MockEngine { request ->
            parameters = request.url.parameters
                .names()
                .associateWith { name ->
                    request.url.parameters[name].orEmpty()
                }

            respond(
                status = HttpStatusCode.OK,
                content = responseJson,
                headers = jsonHeaders()
            )
        }

        val client = createClient(engine)

        client.use { client ->
            val service = UserService(client)

            service.getUserAnswers(
                page = 1,
                pageSize = 20,
                id = 123
            )

            assertEquals("stackoverflow", parameters["site"])
            assertEquals("desc", parameters["order"])
            assertEquals(
                QuestionSort.ACTIVITY.name,
                parameters["sort"]
            )
            assertEquals("1", parameters["page"])
            assertEquals("20", parameters["page_size"])
            assertEquals(USER_ANSWER_FILTER, parameters["filter"])
        }
    }

}
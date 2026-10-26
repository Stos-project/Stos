package com.m4ykey.stos.user

import com.m4ykey.stos.core.Filters.QUESTION_HOME_FILTER
import com.m4ykey.stos.core.Filters.USER_ANSWER_FILTER
import com.m4ykey.stos.core.Filters.USER_FILTER
import com.m4ykey.stos.createClient
import com.m4ykey.stos.jsonHeaders
import com.m4ykey.stos.question.domain.model.QuestionSort
import com.m4ykey.stos.responseJson
import com.m4ykey.stos.user.data.remote.service.UserService
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class UserServiceTest {

    @Test
    fun `getUserById sends correct endpoint and parameters`() = runTest {
        var path = ""
        var filter : String? = null

        val engine = MockEngine { request ->
            path = request.url.encodedPath
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
                filter = "user-filter"
            )

            assertEquals("/users/123", path)
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
                filter = "question-filter",
                order = "desc",
                sort = "activity",
                page = 2,
                pageSize = 20
            )

            assertEquals("/users/123/questions", path)
            assertEquals("desc", parameters["order"])
            assertEquals("activity", parameters["sort"])
            assertEquals("2", parameters["page"])
            assertEquals("20", parameters["pagesize"])
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
                filter = "answer-filter",
                order = "desc",
                sort = "activity",
                page = 2,
                pageSize = 20
            )

            assertEquals("/users/456/answers", path)
            assertEquals("desc", parameters["order"])
            assertEquals("activity", parameters["sort"])
            assertEquals("2", parameters["page"])
            assertEquals("20", parameters["pagesize"])
            assertEquals("answer-filter", parameters["filter"])
            assertEquals(0, result.items.size)
        }
    }

    @Test
    fun `getUserById uses default parameters`() = runTest {
        var filter : String? = null

        val engine = MockEngine { request ->
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

            assertEquals(USER_FILTER, filter)
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

            assertEquals("DESC", parameters["order"])
            assertEquals(
                QuestionSort.ACTIVITY.name.lowercase(),
                parameters["sort"]
            )
            assertEquals("1", parameters["page"])
            assertEquals("20", parameters["pagesize"])
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

            assertEquals("DESC", parameters["order"])
            assertEquals(
                QuestionSort.ACTIVITY.name.lowercase(),
                parameters["sort"]
            )
            assertEquals("1", parameters["page"])
            assertEquals("20", parameters["pagesize"])
            assertEquals(USER_ANSWER_FILTER, parameters["filter"])
        }
    }
}
package com.m4ykey.stos.site

import com.m4ykey.stos.core.Filters
import com.m4ykey.stos.createClient
import com.m4ykey.stos.jsonHeaders
import com.m4ykey.stos.mockEngine
import com.m4ykey.stos.sites.data.remote.service.SiteService
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.ClientRequestException
import io.ktor.client.plugins.ServerResponseException
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class SiteServiceTest {

    private val responseJson = """
        {
            "items": [],
            "has_more": false
        }
    """.trimIndent()

    @Test
    fun `getSites sends correct endpoint and parameters`() = runTest {
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
                content = responseJson,
                status = HttpStatusCode.OK,
                headers = jsonHeaders()
            )
        }

        val client = createClient(engine)

        client.use { client ->
            val service = SiteService(client)

            val result = service.getSites(
                filter = "site-filter",
                page = 2,
                pageSize = 20
            )

            assertEquals("/sites", path)
            assertEquals("2", parameters["page"])
            assertEquals("20", parameters["pagesize"])
            assertEquals("site-filter", parameters["filter"])
            assertEquals(0, result.items.size)
        }
    }

    @Test
    fun `getSites uses get method`() = runTest {
        var method : HttpMethod? = null

        val engine = mockEngine { method = it.method }

        createClient(engine).use { client ->
            SiteService(client).getSites(page = 1, pageSize = 20)
        }

        assertEquals(HttpMethod.Get, method)
    }

    @Test
    fun `getSites uses default filter when not provided`() = runTest {
        var filter : String? = null

        val engine = mockEngine { filter = it.url.parameters["filter"] }

        createClient(engine).use { client ->
            SiteService(client).getSites(page = 1, pageSize = 10)
        }

        assertEquals(Filters.SITES_FILTER, filter)
    }

    @Test
    fun `getSites sends exactly three query parameters`() = runTest {
        var names = emptySet<String>()

        val engine = mockEngine { names = it.url.parameters.names() }

        createClient(engine).use { client ->
            SiteService(client).getSites(filter = "f", page = 1, pageSize = 5)
        }

        assertEquals(setOf("filter", "page", "pagesize"), names)
    }

    @Test
    fun `getSites parses has_more flag`() = runTest {
        val json = """
            {
                "items": [],
                "has_more": true
            }
        """.trimIndent()

        val engine = mockEngine(content = json)

        createClient(engine).use { client ->
            val result = SiteService(client).getSites(page = 1, pageSize = 10)

            assertTrue(result.has_more)
        }
    }

    @Test
    fun `getSites parses items from response`() = runTest {
        val json = """
            {
                "items": [
                    { "name": "Stack Overflow", "api_site_parameter": "stackoverflow" },
                    { "name": "Super User", "api_site_parameter": "superuser" }
                ],
                "has_more": false
            }
        """.trimIndent()

        val engine = mockEngine(content = json)

        createClient(engine).use { client ->
            val result = SiteService(client).getSites(page = 1, pageSize = 20)

            assertEquals(2, result.items.size)
            assertEquals("Stack Overflow", result.items[0].name)
            assertEquals("Super User", result.items[1].name)
        }
    }

    @Test
    fun `getSites throws ClientRequestException on 4xx`() = runTest {
        val engine = mockEngine(status = HttpStatusCode.BadRequest)

        createClient(engine).use { client ->
            assertFailsWith<ClientRequestException> {
                SiteService(client).getSites(page = 1, pageSize = 10)
            }
        }
    }

    @Test
    fun `getSites throws ServerResponseException on 5xx`() = runTest {
        val engine = mockEngine(status = HttpStatusCode.InternalServerError)

        createClient(engine).use { client ->
            assertFailsWith<ServerResponseException> {
                SiteService(client).getSites(page = 1, pageSize = 20)
            }
        }
    }

}
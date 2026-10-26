package com.m4ykey.stos

import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.request.HttpRequestData
import io.ktor.http.HttpStatusCode

fun mockEngine(
    content : String = responseJson,
    status : HttpStatusCode = HttpStatusCode.OK,
    onRequest : (HttpRequestData) -> Unit = {}
) = MockEngine { request ->
    onRequest(request)
    respond(
        content = content,
        status = status,
        headers = jsonHeaders()
    )
}
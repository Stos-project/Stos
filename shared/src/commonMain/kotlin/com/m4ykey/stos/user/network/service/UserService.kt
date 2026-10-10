package com.m4ykey.stos.user.network.service

import com.m4ykey.stos.core.Items
import com.m4ykey.stos.question.network.model.QuestionItemDto
import com.m4ykey.stos.user.network.model.OwnerDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.appendPathSegments

class UserService(private val client : HttpClient) : RemoteUserService {

    override suspend fun getUserById(
        filter: String,
        site: String,
        id: Int
    ): Items<OwnerDto> {
        return client.get {
            url {
                appendPathSegments("users/$id")
                parameter("filter", filter)
                parameter("site", site)
            }
        }.body()
    }

    override suspend fun getUserQuestions(
        site: String,
        id: Int,
        order: String,
        sort: String,
        page: Int,
        filter: String,
        pageSize: Int
    ): Items<QuestionItemDto> {
        return client.get {
            url {
                appendPathSegments("users/$id/questions")
                parameter("site", site)
                parameter("order", order)
                parameter("sort", sort)
                parameter("page", page)
                parameter("page_size", pageSize)
                parameter("filter", filter)
            }
        }.body()
    }


}
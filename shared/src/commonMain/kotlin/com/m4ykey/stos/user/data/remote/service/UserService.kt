package com.m4ykey.stos.user.data.remote.service

import com.m4ykey.stos.core.model.Items
import com.m4ykey.stos.question.data.remote.model.QuestionItemDto
import com.m4ykey.stos.user.data.remote.model.AnswerItemDto
import com.m4ykey.stos.user.data.remote.model.OwnerDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.appendPathSegments

class UserService(private val client : HttpClient) : RemoteUserService {

    override suspend fun getUserById(
        filter: String,
        id: Int
    ): Items<OwnerDto> {
        return client.get {
            url {
                appendPathSegments("users/$id")
                parameter("filter", filter)
            }
        }.body()
    }

    override suspend fun getUserQuestions(
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
                parameter("order", order)
                parameter("sort", sort)
                parameter("page", page)
                parameter("page_size", pageSize)
                parameter("filter", filter)
            }
        }.body()
    }

    override suspend fun getUserAnswers(
        id: Int,
        order: String,
        sort: String,
        page: Int,
        pageSize: Int,
        filter: String
    ) : Items<AnswerItemDto> {
        return client.get {
            url {
                appendPathSegments("users/$id/answers")
                parameter("order", order)
                parameter("sort", sort)
                parameter("page", page)
                parameter("page_size", pageSize)
                parameter("filter", filter)
            }
        }.body()
    }


}
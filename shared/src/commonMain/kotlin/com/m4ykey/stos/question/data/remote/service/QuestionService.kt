package com.m4ykey.stos.question.data.remote.service

import com.m4ykey.stos.core.model.Items
import com.m4ykey.stos.question.data.remote.model.QuestionAnswerDto
import com.m4ykey.stos.question.data.remote.model.QuestionDetailDto
import com.m4ykey.stos.question.data.remote.model.QuestionItemDto
import com.m4ykey.stos.question.domain.model.QuestionSort
import com.m4ykey.stos.question.domain.model.RelatedQuestionSort
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.appendPathSegments

class QuestionService(private val client : HttpClient) : RemoteQuestionService {

    override suspend fun getQuestions(
        order: String,
        sort: String,
        site: String,
        filter: String,
        page: Int,
        pageSize: Int
    ): Items<QuestionItemDto> {
        return client.get {
            url {
                appendPathSegments("questions")
                parameter("order", order)
                parameter("sort", sort)
                parameter("site", site)
                parameter("filter", filter)
                parameter("page", page)
                parameter("page_size", pageSize)
            }
        }.body()
    }

    override suspend fun getQuestionById(
        site: String,
        id: Int,
        filter : String
    ): Items<QuestionDetailDto> {
        return client.get {
            url {
                appendPathSegments("questions/$id")
                parameter("site", site)
                parameter("filter", filter)
            }
        }.body()
    }

    override suspend fun getQuestionAnswers(
        id: Int,
        site: String,
        page: Int,
        pageSize: Int,
        order: String,
        sort: QuestionSort,
        filter: String
    ): Items<QuestionAnswerDto> {
        return client.get {
            url {
                appendPathSegments("questions/$id/answers")
                parameter("site", site)
                parameter("page", page)
                parameter("page_size", pageSize)
                parameter("order", order)
                parameter("sort", sort)
                parameter("filter", filter)
            }
        }.body()
    }

    override suspend fun getQuestionRelated(
        id: Int,
        page: Int,
        pageSize: Int,
        order: String,
        sort: RelatedQuestionSort,
        site: String,
        filter: String
    ): Items<QuestionItemDto> {
        return client.get {
            url {
                appendPathSegments("questions/$id/related")
                parameter("site", site)
                parameter("page", page)
                parameter("page_size", pageSize)
                parameter("order", order)
                parameter("sort", sort)
                parameter("filter", filter)
            }
        }.body()
    }

    override suspend fun getQuestionTag(
        page: Int,
        pageSize: Int,
        order: String,
        sort: String,
        site: String,
        filter: String,
        tagged: String
    ): Items<QuestionItemDto> {
        return client.get {
            url {
                appendPathSegments("questions")
                parameter("page", page)
                parameter("page_size", pageSize)
                parameter("order", order)
                parameter("sort", sort)
                parameter("site", site)
                parameter("filter", filter)
                parameter("tagged", tagged)
            }
        }.body()
    }
}
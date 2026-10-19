package com.m4ykey.stos.question.data.remote.service

import com.m4ykey.stos.core.model.Items
import com.m4ykey.stos.core.network.pagedParameters
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
        filter: String,
        page: Int,
        pageSize: Int
    ): Items<QuestionItemDto> {
        return client.get {
            url { appendPathSegments("questions") }
            pagedParameters(page, pageSize, filter, order, sort)
        }.body()
    }

    override suspend fun getQuestionById(
        id: Int,
        filter : String
    ): Items<QuestionDetailDto> {
        return client.get {
            url {
                appendPathSegments("questions/$id")
                parameter("filter", filter)
            }
        }.body()
    }

    override suspend fun getQuestionAnswers(
        id: Int,
        page: Int,
        pageSize: Int,
        order: String,
        sort: QuestionSort,
        filter: String
    ): Items<QuestionAnswerDto> {
        return client.get {
            url { appendPathSegments("questions/$id/answers") }
            pagedParameters(page, pageSize, filter, order, sort.name)
        }.body()
    }

    override suspend fun getQuestionRelated(
        id: Int,
        page: Int,
        pageSize: Int,
        order: String,
        sort: RelatedQuestionSort,
        filter: String
    ): Items<QuestionItemDto> {
        return client.get {
            url { appendPathSegments("questions/$id/related") }
            pagedParameters(page, pageSize, filter, order, sort.name)
        }.body()
    }

    override suspend fun getQuestionTag(
        page: Int,
        pageSize: Int,
        order: String,
        sort: String,
        filter: String,
        tagged: String
    ): Items<QuestionItemDto> {
        return client.get {
            url { appendPathSegments("questions") }
            parameter("tagged", tagged)
            pagedParameters(page, pageSize, filter, order, sort)
        }.body()
    }
}
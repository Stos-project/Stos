package com.m4ykey.stos.search.data.remote.service

import com.m4ykey.stos.core.model.Items
import com.m4ykey.stos.question.data.remote.model.QuestionItemDto
import com.m4ykey.stos.search.data.remote.model.TagsItemDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.appendPathSegments

class SearchService(private val client : HttpClient) : RemoteSearchService {

    override suspend fun searchQuestions(
        order: String,
        sort: String,
        site: String,
        filter: String,
        page: Int,
        pageSize: Int,
        inTitle: String
    ): Items<QuestionItemDto> {
        return client.get {
            url {
                appendPathSegments("search")
                parameter("order", order)
                parameter("sort", sort)
                parameter("site", site)
                parameter("filter", filter)
                parameter("page", page)
                parameter("page_size", pageSize)
                parameter("intitle", inTitle)
            }
        }.body()
    }

    override suspend fun getTags(
        page: Int,
        pageSize: Int,
        order: String,
        sort: String,
        site: String,
        filter: String
    ): Items<TagsItemDto> {
        return client.get {
            url {
                appendPathSegments("/tags")
                parameter("page", page)
                parameter("page_size", pageSize)
                parameter("order", order)
                parameter("sort", sort)
                parameter("filter", filter)
                parameter("site", site)
            }
        }.body()
    }
}
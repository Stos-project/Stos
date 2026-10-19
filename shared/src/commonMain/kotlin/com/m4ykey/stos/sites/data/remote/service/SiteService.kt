package com.m4ykey.stos.sites.data.remote.service

import com.m4ykey.stos.core.model.Items
import com.m4ykey.stos.sites.data.remote.model.SitesItemDto
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.appendPathSegments

class SiteService(private val client : HttpClient) : RemoteSiteService {

    override suspend fun getSites(
        filter: String,
        page : Int,
        pageSize : Int
    ): Items<SitesItemDto> {
        return client.get {
            url {
                appendPathSegments("sites")
                parameter("filter", filter)
                parameter("page", page)
                parameter("page_size", pageSize)
            }
        }.body()
    }
}
package com.m4ykey.stos.site

import com.m4ykey.stos.core.model.Items
import com.m4ykey.stos.sites.data.remote.model.SitesItemDto
import com.m4ykey.stos.sites.data.remote.service.RemoteSiteService

class FakeRemoteSiteServicePaging(
    private val siteHasMore: Boolean = false,
    private val siteError : Exception? = null
) : RemoteSiteService {

    var sitePage : Int? = null
        private set

    var sitePageSize : Int? = null
        private set

    override suspend fun getSites(
        filter: String,
        page: Int,
        pageSize: Int
    ): Items<SitesItemDto> {
        sitePage = page
        sitePageSize = pageSize

        siteError?.let { throw it }

        return Items(
            items = emptyList(),
            has_more = siteHasMore
        )
    }
}
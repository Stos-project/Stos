package com.m4ykey.stos.site

import com.m4ykey.stos.core.model.Items
import com.m4ykey.stos.sites.data.remote.model.SitesItemDto
import com.m4ykey.stos.sites.data.remote.service.RemoteSiteService
import com.m4ykey.stos.sites.data.repository.SiteRepositoryImpl
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertNotNull

class SiteRepositoryImplTest {

    private class FakeRemoteSiteService(
        private val sitesResponse : Items<SitesItemDto> = Items(emptyList()),
        private val sitesError : Exception? = null
    ) : RemoteSiteService {

        var getSitesCalls = 0
            private set

        override suspend fun getSites(
            filter: String,
            page: Int,
            pageSize: Int
        ): Items<SitesItemDto> {
            getSitesCalls++
            sitesError?.let { throw it }
            return sitesResponse
        }
    }

    private val sitesItemDto = SitesItemDto(
        faviconUrl = "https://example.com/favicon.png",
        iconUrl = "https://example.com/icon.png",
        audience = "professional programmers",
        siteUrl = "https://stackoverflow.com",
        apiSiteParameter = "stackoverflow",
        logoUrl = "https://example.com/logo.png",
        name = "Stack Overflow"
    )

    @Test
    fun `getSites returns flow emitting PagingData`() = runTest {
        val service = FakeRemoteSiteService(
            sitesResponse = Items(listOf(sitesItemDto))
        )
        val repository = SiteRepositoryImpl(service)

        val pagingData = repository.getSites().first()

        assertNotNull(pagingData)
    }

    @Test
    fun `getSites returns non null flow`() = runTest {
        val service = FakeRemoteSiteService()
        val repository = SiteRepositoryImpl(service)

        val flow = repository.getSites()

        assertNotNull(flow)
    }

}
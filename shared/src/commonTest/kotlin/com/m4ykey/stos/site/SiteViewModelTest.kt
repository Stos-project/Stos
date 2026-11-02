@file:OptIn(ExperimentalCoroutinesApi::class)

package com.m4ykey.stos.site

import com.m4ykey.stos.sites.data.helpers.SiteManager
import com.m4ykey.stos.sites.data.remote.model.SitesItemDto
import com.m4ykey.stos.sites.domain.model.SitesItem
import com.m4ykey.stos.sites.presentation.SitesViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNotNull

class SiteViewModelTest {

    private lateinit var viewModel : SitesViewModel
    private lateinit var repository : FakeSiteRepository
    private lateinit var siteManager: SiteManager

    private fun createViewModel() {
        siteManager = SiteManager()
        repository = FakeSiteRepository()
        viewModel = SitesViewModel(repository = repository, siteManager = siteManager)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    private val sitesItem = SitesItem(
        faviconUrl = "https://example.com/favicon.png",
        iconUrl = "https://example.com/icon.png",
        audience = "professional programmers",
        siteUrl = "https://serverfault.com",
        apiSiteParameter = "serverfault",
        logoUrl = "https://example.com/logo.png",
        name = "Server Fault"
    )

    @Test
    fun `initial selected site is stackoverflow`() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        createViewModel()

        assertEquals("stackoverflow", viewModel.selectedSite.value)
    }

    @Test
    fun `selectSite updates selected site`() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        createViewModel()

        viewModel.selectSite(sitesItem)

        assertEquals("serverfault", viewModel.selectedSite.value)
    }

    @Test
    fun `sites flow emits pagingdata when collected`() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        createViewModel()

        val pagingData = viewModel.sites.first()

        assertNotNull(pagingData)
    }

}
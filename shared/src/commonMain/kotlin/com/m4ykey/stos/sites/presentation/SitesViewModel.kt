@file:OptIn(ExperimentalCoroutinesApi::class)

package com.m4ykey.stos.sites.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.m4ykey.stos.sites.data.helpers.SiteManager
import com.m4ykey.stos.sites.domain.model.SelectedSite
import com.m4ykey.stos.sites.domain.model.SitesItem
import com.m4ykey.stos.sites.domain.repository.SiteRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest

class SitesViewModel(
    private val siteManager: SiteManager,
    repository : SiteRepository
) : ViewModel() {

    val selectedSite = siteManager.selectedSite

    val sites : Flow<PagingData<SitesItem>> = repository.getSites()
        .cachedIn(viewModelScope)

    fun selectSite(site : SitesItem) {
        siteManager.updateSite(
            SelectedSite(
                apiSiteParameter = site.apiSiteParameter,
                name = site.name
            )
        )
    }

}
@file:OptIn(ExperimentalCoroutinesApi::class)

package com.m4ykey.stos.sites.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.m4ykey.stos.sites.data.helpers.SiteManager
import com.m4ykey.stos.sites.domain.model.SitesItem
import com.m4ykey.stos.sites.domain.repository.SiteRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapLatest

class SitesViewModel(
    private val siteManager: SiteManager,
    private val repository : SiteRepository
) : ViewModel() {

    val selectedSite = siteManager.selectedSite

    val sites : Flow<PagingData<SitesItem>> = siteManager.selectedSite
        .flatMapLatest { _ ->
            repository.getSites()
        }
        .cachedIn(viewModelScope)

    fun selectSite(site : SitesItem) {
        siteManager.updateSite(site.apiSiteParameter)
    }

}
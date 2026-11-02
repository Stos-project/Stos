package com.m4ykey.stos.site

import androidx.paging.PagingData
import com.m4ykey.stos.sites.domain.model.SitesItem
import com.m4ykey.stos.sites.domain.repository.SiteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeSiteRepository : SiteRepository {

    var siteFlow : Flow<PagingData<SitesItem>> = flowOf(PagingData.empty())

    override fun getSites(): Flow<PagingData<SitesItem>> {
        return siteFlow
    }

}
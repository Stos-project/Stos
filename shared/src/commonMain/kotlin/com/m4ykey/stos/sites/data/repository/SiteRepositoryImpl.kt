package com.m4ykey.stos.sites.data.repository

import androidx.paging.Pager
import androidx.paging.PagingData
import com.m4ykey.stos.core.paging.pagingConfig
import com.m4ykey.stos.sites.data.remote.paging.SitePagingSource
import com.m4ykey.stos.sites.data.remote.service.RemoteSiteService
import com.m4ykey.stos.sites.domain.model.SitesItem
import com.m4ykey.stos.sites.domain.repository.SiteRepository
import kotlinx.coroutines.flow.Flow

class SiteRepositoryImpl(private val service : RemoteSiteService) : SiteRepository {

    override fun getSites(): Flow<PagingData<SitesItem>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = {
                SitePagingSource(service)
            }
        ).flow
    }
}
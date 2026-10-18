package com.m4ykey.stos.sites.domain.repository

import androidx.paging.PagingData
import com.m4ykey.stos.sites.domain.model.SitesItem
import kotlinx.coroutines.flow.Flow

interface SiteRepository {

    fun getSites() : Flow<PagingData<SitesItem>>

}
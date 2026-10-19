package com.m4ykey.stos.sites.data.remote.paging

import com.m4ykey.stos.core.network.ApiResult
import com.m4ykey.stos.core.network.safeApi
import com.m4ykey.stos.core.paging.BasePagingSource
import com.m4ykey.stos.core.paging.PageResult
import com.m4ykey.stos.sites.data.mapper.toSites
import com.m4ykey.stos.sites.data.remote.service.RemoteSiteService
import com.m4ykey.stos.sites.domain.model.SitesItem

class SitePagingSource(private val service : RemoteSiteService) : BasePagingSource<SitesItem>() {

    override suspend fun loadData(
        page: Int,
        pageSize: Int
    ): Result<PageResult<SitesItem>> {
        return safeApi {
            service.getSites(page = page, pageSize = pageSize)
        }.run {
            when (this) {
                is ApiResult.Failure -> Result.failure(exception)
                is ApiResult.Success -> {
                    val items = data.items.map { it.toSites() }
                    Result.success(PageResult(items, data.has_more))
                }
            }
        }
    }
}
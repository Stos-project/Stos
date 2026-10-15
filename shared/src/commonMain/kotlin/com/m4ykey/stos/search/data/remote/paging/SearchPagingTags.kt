package com.m4ykey.stos.search.data.remote.paging

import com.m4ykey.stos.core.network.ApiResult
import com.m4ykey.stos.core.network.safeApi
import com.m4ykey.stos.core.paging.BasePagingSource
import com.m4ykey.stos.core.paging.PageResult
import com.m4ykey.stos.search.data.mapper.toTagsItem
import com.m4ykey.stos.search.data.remote.service.RemoteSearchService
import com.m4ykey.stos.search.domain.model.TagsItem

class SearchPagingTags(
    private val service : RemoteSearchService
) : BasePagingSource<TagsItem>() {

    override suspend fun loadData(
        page: Int,
        pageSize: Int
    ): Result<PageResult<TagsItem>> {
        return safeApi {
            service.getTags(page = page, pageSize = pageSize)
        }.run {
            when (this) {
                is ApiResult.Failure -> Result.failure(exception)
                is ApiResult.Success -> {
                    val items = data.items.map { it.toTagsItem() }
                    Result.success(PageResult(items, hasMore = data.has_more))
                }
            }
        }
    }
}
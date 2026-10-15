package com.m4ykey.stos.user.data.remote.paging

import com.m4ykey.stos.core.network.ApiResult
import com.m4ykey.stos.core.network.safeApi
import com.m4ykey.stos.core.paging.BasePagingSource
import com.m4ykey.stos.core.paging.PageResult
import com.m4ykey.stos.user.domain.model.AnswerItem
import com.m4ykey.stos.user.data.mapper.toAnswerItem
import com.m4ykey.stos.user.data.remote.service.RemoteUserService

class UserAnswerPagingSource(
    private val service : RemoteUserService,
    private val id : Int
) : BasePagingSource<AnswerItem>() {

    override suspend fun loadData(
        page: Int,
        pageSize: Int
    ): Result<PageResult<AnswerItem>> {
        return safeApi {
            service.getUserAnswers(
                page = page,
                pageSize = pageSize,
                id = id
            )
        }.run {
            when (this) {
                is ApiResult.Failure -> Result.failure(exception)
                is ApiResult.Success -> {
                    val items = data.items.map { it.toAnswerItem() }
                    Result.success(PageResult(items, hasMore = data.has_more))
                }
            }
        }
    }
}
package com.m4ykey.stos.user.data.remote.paging

import com.m4ykey.stos.core.network.ApiResult
import com.m4ykey.stos.core.network.safeApi
import com.m4ykey.stos.core.paging.BasePagingSource
import com.m4ykey.stos.core.paging.PageResult
import com.m4ykey.stos.question.domain.model.QuestionItem
import com.m4ykey.stos.question.data.mapper.toQuestionItem
import com.m4ykey.stos.user.data.remote.service.RemoteUserService

class UserQuestionPagingSource(
    private val service : RemoteUserService,
    private val id : Int
) : BasePagingSource<QuestionItem>() {

    override suspend fun loadData(
        page: Int,
        pageSize: Int
    ): Result<PageResult<QuestionItem>> {
        return safeApi {
            service.getUserQuestions(
                page = page,
                pageSize = pageSize,
                id = id
            )
        }.run {
            when (this) {
                is ApiResult.Failure -> Result.failure(exception)
                is ApiResult.Success -> {
                    val items = data.items.map { it.toQuestionItem() }
                    Result.success(PageResult(items, hasMore = data.has_more))
                }
            }
        }
    }
}
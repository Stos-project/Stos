package com.m4ykey.stos.question.data.remote.paging

import com.m4ykey.stos.core.network.ApiResult
import com.m4ykey.stos.core.network.safeApi
import com.m4ykey.stos.core.paging.BasePagingSource
import com.m4ykey.stos.core.paging.PageResult
import com.m4ykey.stos.question.data.mapper.toQuestionItem
import com.m4ykey.stos.question.data.remote.service.RemoteQuestionService
import com.m4ykey.stos.question.domain.model.QuestionItem

class QuestionTagPagingSource(
    private val service : RemoteQuestionService,
    private val tagged : String,
    private val sort : String
) : BasePagingSource<QuestionItem>() {

    override suspend fun loadData(
        page: Int,
        pageSize: Int
    ): Result<PageResult<QuestionItem>> {
        return safeApi {
            service.getQuestionTag(page = page, sort = sort, pageSize = pageSize, tagged = tagged)
        }.run {
            when (this) {
                is ApiResult.Success -> {
                    val items = data.items.map { it.toQuestionItem() }
                    Result.success(PageResult(items, data.has_more))
                }
                is ApiResult.Failure -> Result.failure(exception)
            }
        }
    }
}
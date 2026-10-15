package com.m4ykey.stos.question.data.remote.paging

import com.m4ykey.stos.core.network.ApiResult
import com.m4ykey.stos.core.network.safeApi
import com.m4ykey.stos.core.paging.BasePagingSource
import com.m4ykey.stos.core.paging.PageResult
import com.m4ykey.stos.question.data.mapper.toQuestionAnswer
import com.m4ykey.stos.question.data.remote.service.RemoteQuestionService
import com.m4ykey.stos.question.domain.model.QuestionAnswer

class QuestionAnswerPagingSource(
    private val id : Int,
    private val service : RemoteQuestionService
) : BasePagingSource<QuestionAnswer>() {

    override suspend fun loadData(
        page: Int,
        pageSize: Int
    ): Result<PageResult<QuestionAnswer>> {
        return safeApi {
            service.getQuestionAnswers(page = page, pageSize = pageSize, id = id)
        }.run {
            when (this) {
                is ApiResult.Success -> {
                    val items = data.items.map { it.toQuestionAnswer() }
                    Result.success(PageResult(items, hasMore = data.has_more))
                }
                is ApiResult.Failure -> Result.failure(exception)
            }
        }
    }
}
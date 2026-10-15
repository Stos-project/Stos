package com.m4ykey.stos.question.network.paging

import com.m4ykey.stos.core.network.ApiResult
import com.m4ykey.stos.core.network.safeApi
import com.m4ykey.stos.core.paging.BasePagingSource
import com.m4ykey.stos.core.paging.PageResult
import com.m4ykey.stos.navigation.Route
import com.m4ykey.stos.question.domain.model.QuestionItem
import com.m4ykey.stos.question.domain.model.RelatedQuestionSort
import com.m4ykey.stos.question.mapper.toQuestionItem
import com.m4ykey.stos.question.network.service.RemoteQuestionService

class QuestionRelatedPagingSource(
    private val service : RemoteQuestionService,
    private val sort : RelatedQuestionSort,
    private val id : Int
) : BasePagingSource<QuestionItem>(){

    override suspend fun loadData(page: Int, pageSize: Int): Result<PageResult<QuestionItem>> {
        return safeApi {
            service.getQuestionRelated(page=page, pageSize = pageSize, sort = sort, id = id)
        }.run {
            when (this){
                is ApiResult.Failure -> Result.failure(exception)
                is ApiResult.Success ->{
                    val items = data.items.map { it.toQuestionItem() }
                    Result.success(PageResult(items, hasMore = data.has_more))
                }
            }
        }
    }
}
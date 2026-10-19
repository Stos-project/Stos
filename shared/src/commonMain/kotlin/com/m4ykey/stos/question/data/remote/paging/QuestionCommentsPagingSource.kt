package com.m4ykey.stos.question.data.remote.paging

import com.m4ykey.stos.core.network.ApiResult
import com.m4ykey.stos.core.network.safeApi
import com.m4ykey.stos.core.paging.BasePagingSource
import com.m4ykey.stos.core.paging.PageResult
import com.m4ykey.stos.question.data.mapper.toQuestionComment
import com.m4ykey.stos.question.data.remote.service.RemoteQuestionService
import com.m4ykey.stos.question.domain.model.QuestionComments

class QuestionCommentsPagingSource (
    private val id :Int,
    private val service : RemoteQuestionService
) : BasePagingSource<QuestionComments>(){
    override suspend fun loadData(
        page: Int,
        pageSize: Int
    ): Result<PageResult<QuestionComments>> {
        return safeApi {
            service.getQuestionComments(page = page, pageSize = pageSize, id = id)
        }.run {
            when (this){
                is ApiResult.Success ->{
                    val items = data.items.map { it.toQuestionComment() }
                    Result.success(PageResult(items, hasMore = data.has_more))
                }
                is ApiResult.Failure -> Result.failure(exception)
            }
        }
    }


}

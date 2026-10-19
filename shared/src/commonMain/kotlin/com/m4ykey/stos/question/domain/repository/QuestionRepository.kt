package com.m4ykey.stos.question.domain.repository

import androidx.paging.PagingData
import com.m4ykey.stos.core.network.ApiResult
import com.m4ykey.stos.question.domain.model.QuestionAnswer
import com.m4ykey.stos.question.domain.model.QuestionComments
import com.m4ykey.stos.question.domain.model.QuestionDetail
import com.m4ykey.stos.question.domain.model.QuestionItem
import com.m4ykey.stos.question.domain.model.RelatedQuestionSort
import kotlinx.coroutines.flow.Flow

interface QuestionRepository {

    fun getQuestions(sort : String) : Flow<PagingData<QuestionItem>>
    suspend fun getQuestionById(questionId : Int) : Flow<ApiResult<QuestionDetail>>
    fun getQuestionAnswers(questionId : Int) : Flow<PagingData<QuestionAnswer>>
    fun getRelatedQuestions(sort: RelatedQuestionSort, questionId: Int) : Flow<PagingData<QuestionItem>>
    fun getQuestionTag(sort : String, tagged : String) : Flow<PagingData<QuestionItem>>

    fun getQuestionComments(id : Int) : Flow<PagingData<QuestionComments>>
}
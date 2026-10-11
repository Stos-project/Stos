package com.m4ykey.stos.user.domain.repository

import androidx.paging.PagingData
import com.m4ykey.stos.core.network.ApiResult
import com.m4ykey.stos.question.domain.model.QuestionItem
import com.m4ykey.stos.user.domain.model.AnswerItem
import com.m4ykey.stos.user.domain.model.Owner
import kotlinx.coroutines.flow.Flow

interface UserRepository {

    suspend fun getUserById(userId : Int) : Flow<ApiResult<Owner>>
    fun getUserQuestions(userId : Int) : Flow<PagingData<QuestionItem>>
    fun getUserAnswers(userId : Int) : Flow<PagingData<AnswerItem>>

}
package com.m4ykey.stos.user

import androidx.paging.PagingData
import com.m4ykey.stos.core.network.ApiResult
import com.m4ykey.stos.question.domain.model.QuestionItem
import com.m4ykey.stos.user.domain.model.AnswerItem
import com.m4ykey.stos.user.domain.model.Owner
import com.m4ykey.stos.user.domain.repository.UserRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeUserRepository : UserRepository {

    var userResult : Flow<ApiResult<Owner>> = flowOf(ApiResult.Success(Owner.EMPTY))

    var questionFlow : Flow<PagingData<QuestionItem>> = flowOf(PagingData.empty())

    var answersFlow : Flow<PagingData<AnswerItem>> = flowOf(PagingData.empty())

    override suspend fun getUserById(userId: Int): Flow<ApiResult<Owner>> = userResult

    override fun getUserQuestions(userId: Int): Flow<PagingData<QuestionItem>> = questionFlow

    override fun getUserAnswers(userId: Int): Flow<PagingData<AnswerItem>> = answersFlow

}
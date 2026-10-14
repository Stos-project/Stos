package com.m4ykey.stos.user.data.repository

import androidx.paging.Pager
import androidx.paging.PagingData
import com.m4ykey.stos.core.network.ApiResult
import com.m4ykey.stos.core.network.safeApi
import com.m4ykey.stos.core.paging.pagingConfig
import com.m4ykey.stos.question.domain.model.QuestionItem
import com.m4ykey.stos.user.domain.model.AnswerItem
import com.m4ykey.stos.user.domain.model.Owner
import com.m4ykey.stos.user.domain.repository.UserRepository
import com.m4ykey.stos.user.data.mapper.toOwner
import com.m4ykey.stos.user.data.remote.paging.UserAnswerPagingSource
import com.m4ykey.stos.user.data.remote.paging.UserQuestionPagingSource
import com.m4ykey.stos.user.data.remote.service.UserService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn

class UserRepositoryImpl(
    private val service: UserService
) : UserRepository {

    override suspend fun getUserById(userId: Int): Flow<ApiResult<Owner>> {
        return flow {
            val result = safeApi { service.getUserById(id = userId) }

            when (result) {
                is ApiResult.Failure -> {
                    emit(ApiResult.Failure(result.exception))
                }
                is ApiResult.Success -> {
                    val user = result.data.items.map { it.toOwner() }.firstOrNull()
                    if (user != null) {
                        emit(ApiResult.Success(user))
                    }
                }
            }
        }.flowOn(Dispatchers.IO)
    }

    override fun getUserQuestions(userId: Int): Flow<PagingData<QuestionItem>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = {
                UserQuestionPagingSource(service, userId)
            }
        ).flow
    }

    override fun getUserAnswers(userId: Int): Flow<PagingData<AnswerItem>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = {
                UserAnswerPagingSource(service, userId)
            }
        ).flow
    }
}
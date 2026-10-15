@file:OptIn(ExperimentalCoroutinesApi::class)

package com.m4ykey.stos.user.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import com.m4ykey.stos.core.network.ApiResult
import com.m4ykey.stos.user.domain.model.UserProfileItem
import com.m4ykey.stos.user.domain.model.UserProfileItems
import com.m4ykey.stos.user.domain.repository.UserRepository
import com.m4ykey.stos.user.presentation.state.UserSortState
import com.m4ykey.stos.user.presentation.state.UserState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class UserViewModel(
    private val repository: UserRepository
) : ViewModel() {

    private val _userState = MutableStateFlow(UserState())
    val userState = _userState.asStateFlow()

    private val _userSortState = MutableStateFlow(UserSortState())
    val userSortState = _userSortState.asStateFlow()

    private val _userId = MutableStateFlow<Int?>(null)

    fun getUserById(userId : Int) {
        viewModelScope.launch {
            repository.getUserById(userId)
                .onStart {
                    _userState.update {
                        it.copy(
                            isLoading = true,
                            error = null
                        )
                    }
                }
                .catch { e ->
                    _userState.update {
                        it.copy(
                            isLoading = false,
                            error = e.message
                        )
                    }
                }
                .collect { result ->
                    when (result) {
                        is ApiResult.Success -> {
                            _userState.update {
                                it.copy(
                                    isLoading = false,
                                    error = null,
                                    user = result.data
                                )
                            }
                        }
                        is ApiResult.Failure -> {
                            _userState.update {
                                it.copy(
                                    isLoading = false,
                                    error = result.exception.message
                                )
                            }
                        }
                    }
                }
        }
    }

    val userQuestionItems : Flow<PagingData<UserProfileItem>> =
        combine(
            _userId,
            userSortState.map { it.sort }.distinctUntilChanged()
        ) { userId, sort ->
            userId to sort
        }.flatMapLatest { (userId, sort) ->
            when {
                userId == null -> {
                    emptyFlow()
                }

                sort == UserProfileItems.QUESTIONS ->
                    repository.getUserQuestions(userId).map { pagingData ->
                        pagingData.map { question ->
                            UserProfileItem.Question(question) as UserProfileItem
                        }
                    }

                else ->
                    repository.getUserAnswers(userId).map { pagingData ->
                        pagingData.map { answer ->
                            UserProfileItem.Answer(answer) as UserProfileItem
                        }
                    }
            }
        }.cachedIn(viewModelScope)

    fun loadUserItems(userId : Int) {
        _userId.value = userId
    }

    fun updateSort(sort : UserProfileItems) {
        _userSortState.update { it.copy(sort = sort) }
    }

    fun onRetryUserState(userId : Int) {
        getUserById(userId)
    }

}
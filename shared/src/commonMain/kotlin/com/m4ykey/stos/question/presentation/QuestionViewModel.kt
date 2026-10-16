@file:OptIn(ExperimentalCoroutinesApi::class)

package com.m4ykey.stos.question.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.m4ykey.stos.core.network.ApiResult
import com.m4ykey.stos.question.domain.model.QuestionAnswer
import com.m4ykey.stos.question.domain.model.QuestionItem
import com.m4ykey.stos.question.domain.model.QuestionSort
import com.m4ykey.stos.question.domain.model.RelatedQuestionSort
import com.m4ykey.stos.question.domain.repository.QuestionRepository
import com.m4ykey.stos.question.presentation.state.QuestionDetailState
import com.m4ykey.stos.question.presentation.state.QuestionRelatedState
import com.m4ykey.stos.question.presentation.state.QuestionState
import com.m4ykey.stos.question.presentation.state.QuestionTagState
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class QuestionViewModel(
    private val repository : QuestionRepository
) : ViewModel() {

    private val _questionState = MutableStateFlow(QuestionState())
    val questionState = _questionState.asStateFlow()

    private val _questionDetailState = MutableStateFlow(QuestionDetailState())
    val questionDetailState = _questionDetailState.asStateFlow()

    private val _questionRelatedState = MutableStateFlow(QuestionRelatedState())
    val questionRelatedState = _questionRelatedState.asStateFlow()

    private val _questionTagState = MutableStateFlow(QuestionTagState())
    val questionTagState = _questionTagState.asStateFlow()

    private val relatedQuestionId = MutableStateFlow<Int?>(null)
    private val answersQuestionId = MutableStateFlow<Int?>(null)

    private val taggedName = MutableStateFlow<String?>(null)

    private var detailJob : Job? = null

    fun getQuestionDetail(questionId : Int) {
        detailJob?.cancel()
        detailJob = viewModelScope.launch {
            repository.getQuestionById(questionId)
                .onStart {
                    _questionDetailState.update {
                        it.copy(isLoading = true, error = null)
                    }
                }
                .catch { e ->
                    _questionDetailState.update {
                        it.copy(isLoading = false, error = e.message)
                    }
                }
                .collect { result ->
                    when (result) {
                        is ApiResult.Success -> {
                            _questionDetailState.update {
                                it.copy(isLoading = false, error = null, item = result.data)
                            }
                        }
                        is ApiResult.Failure -> {
                            _questionDetailState.update {
                                it.copy(isLoading = false, error = result.exception.message)
                            }
                        }
                    }
                }
        }
    }

    fun onRetryDetailState(questionId : Int) {
        getQuestionDetail(questionId)
    }

    private val answersFlow : Flow<PagingData<QuestionAnswer>> = answersQuestionId
        .filterNotNull()
        .distinctUntilChanged()
        .flatMapLatest { id ->
            repository.getQuestionAnswers(id)
        }
        .cachedIn(viewModelScope)

    private val questionFlow : Flow<PagingData<QuestionItem>> = _questionState
        .map { it.sort }
        .distinctUntilChanged()
        .flatMapLatest { sort ->
            repository.getQuestions(sort = sort.name)
        }
        .cachedIn(viewModelScope)

    private val relatedQuestionsFlow : Flow<PagingData<QuestionItem>> = combine(
        relatedQuestionId.filterNotNull(),
        _questionRelatedState.map { it.sort }.distinctUntilChanged()
    ) { id, sort -> id to sort }
        .flatMapLatest { (id, sort) ->
            repository.getRelatedQuestions(sort = sort, questionId = id)
        }
        .cachedIn(viewModelScope)

    private val tagQuestionFlow : Flow<PagingData<QuestionItem>> = combine(
        taggedName.filterNotNull(),
        _questionTagState.map { it.sort }.distinctUntilChanged()
    ) { tag, sort -> tag to sort }
        .flatMapLatest { (tag, sort) ->
            repository.getQuestionTag(sort = sort.name, tagged = tag)
        }
        .cachedIn(viewModelScope)

    fun getQuestionsTag() : Flow<PagingData<QuestionItem>> = tagQuestionFlow

    fun getQuestions() : Flow<PagingData<QuestionItem>> = questionFlow

    fun getRelatedQuestions() : Flow<PagingData<QuestionItem>> = relatedQuestionsFlow

    fun getQuestionAnswers() : Flow<PagingData<QuestionAnswer>> = answersFlow

    fun setTag(tag : String) {
        taggedName.value = tag
    }

    fun setAnswersQuestionId(id : Int) {
        answersQuestionId.value = id
    }

    fun updateSort(sort : QuestionSort) {
        _questionState.update { it.copy(sort = sort) }
    }

    fun updateRelatedSort(sort : RelatedQuestionSort){
        _questionRelatedState.update { it.copy(sort = sort) }
    }

    fun updateTagSort(sort : QuestionSort) {
        _questionTagState.update { it.copy(sort = sort) }
    }

    fun setRelatedQuestionId(id : Int) {
        relatedQuestionId.value = id
    }
}
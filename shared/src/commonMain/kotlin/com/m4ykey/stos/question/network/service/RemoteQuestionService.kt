package com.m4ykey.stos.question.network.service

import com.m4ykey.stos.core.Filters.QUESTION_ANSWER_FILTER
import com.m4ykey.stos.core.Filters.QUESTION_DETAIL_FILTER
import com.m4ykey.stos.core.Filters.QUESTION_HOME_FILTER
import com.m4ykey.stos.core.Items
import com.m4ykey.stos.question.domain.model.QuestionSort
import com.m4ykey.stos.question.domain.model.RelatedQuestionSort
import com.m4ykey.stos.question.network.model.QuestionAnswerDto
import com.m4ykey.stos.question.network.model.QuestionDetailDto
import com.m4ykey.stos.question.network.model.QuestionItemDto

interface RemoteQuestionService {

    suspend fun getQuestions(
        order : String = "desc",
        sort : String,
        site : String = "stackoverflow",
        filter : String = QUESTION_HOME_FILTER,
        page : Int,
        pageSize : Int
    ) : Items<QuestionItemDto>

    suspend fun getQuestionById(
        site : String = "stackoverflow",
        id : Int,
        filter : String = QUESTION_DETAIL_FILTER
    ) : Items<QuestionDetailDto>

    suspend fun getQuestionAnswers(
        id : Int,
        site : String = "stackoverflow",
        page : Int,
        pageSize : Int,
        order : String = "desc",
        sort : QuestionSort = QuestionSort.ACTIVITY,
        filter : String = QUESTION_ANSWER_FILTER
    ) : Items<QuestionAnswerDto>

    suspend fun getQuestionRelated(
        id : Int,
        page : Int,
        pageSize : Int,
        order : String = "desc",
        sort : RelatedQuestionSort = RelatedQuestionSort.ACTIVITY,
        site : String = "stackoverflow",
        filter : String = QUESTION_HOME_FILTER
    ) : Items<QuestionItemDto>

}
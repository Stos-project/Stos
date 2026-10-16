package com.m4ykey.stos.question.data.remote.service

import com.m4ykey.stos.core.Filters.QUESTION_ANSWER_FILTER
import com.m4ykey.stos.core.Filters.QUESTION_DETAIL_FILTER
import com.m4ykey.stos.core.Filters.QUESTION_HOME_FILTER
import com.m4ykey.stos.core.model.Items
import com.m4ykey.stos.core.model.Order
import com.m4ykey.stos.question.data.remote.model.QuestionAnswerDto
import com.m4ykey.stos.question.data.remote.model.QuestionDetailDto
import com.m4ykey.stos.question.data.remote.model.QuestionItemDto
import com.m4ykey.stos.question.domain.model.QuestionSort
import com.m4ykey.stos.question.domain.model.RelatedQuestionSort

interface RemoteQuestionService {

    suspend fun getQuestions(
        order : String = Order.DESC.name,
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
        order : String = Order.DESC.name,
        sort : QuestionSort = QuestionSort.ACTIVITY,
        filter : String = QUESTION_ANSWER_FILTER
    ) : Items<QuestionAnswerDto>

    suspend fun getQuestionRelated(
        id : Int,
        page : Int,
        pageSize : Int,
        order : String = Order.DESC.name,
        sort : RelatedQuestionSort = RelatedQuestionSort.ACTIVITY,
        site : String = "stackoverflow",
        filter : String = QUESTION_HOME_FILTER
    ) : Items<QuestionItemDto>

    suspend fun getQuestionTag(
        page : Int,
        pageSize : Int,
        order : String = Order.DESC.name,
        sort: String,
        site : String = "stackoverflow",
        filter : String = QUESTION_HOME_FILTER,
        tagged : String
    ) : Items<QuestionItemDto>

}
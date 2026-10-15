package com.m4ykey.stos.user.data.remote.service

import com.m4ykey.stos.core.Filters.QUESTION_HOME_FILTER
import com.m4ykey.stos.core.Filters.USER_ANSWER_FILTER
import com.m4ykey.stos.core.Filters.USER_FILTER
import com.m4ykey.stos.core.model.Items
import com.m4ykey.stos.question.data.remote.model.QuestionItemDto
import com.m4ykey.stos.question.domain.model.QuestionSort
import com.m4ykey.stos.user.data.remote.model.AnswerItemDto
import com.m4ykey.stos.user.data.remote.model.OwnerDto

interface RemoteUserService {

    suspend fun getUserById(
        filter : String = USER_FILTER,
        site : String = "stackoverflow",
        id : Int
    ) : Items<OwnerDto>

    suspend fun getUserQuestions(
        site : String = "stackoverflow",
        id : Int,
        order : String = "desc",
        sort : String = QuestionSort.ACTIVITY.name,
        page : Int,
        filter : String = QUESTION_HOME_FILTER,
        pageSize : Int
    ) : Items<QuestionItemDto>

    suspend fun getUserAnswers(
        site : String = "stackoverflow",
        id : Int,
        order : String = "desc",
        sort: String = QuestionSort.ACTIVITY.name,
        page : Int,
        pageSize: Int,
        filter : String = USER_ANSWER_FILTER
    ) : Items<AnswerItemDto>

}
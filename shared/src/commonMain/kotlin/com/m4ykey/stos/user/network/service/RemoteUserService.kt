package com.m4ykey.stos.user.network.service

import com.m4ykey.stos.core.Filters.QUESTION_HOME_FILTER
import com.m4ykey.stos.core.Filters.USER_FILTER
import com.m4ykey.stos.core.Items
import com.m4ykey.stos.question.domain.model.QuestionSort
import com.m4ykey.stos.question.network.model.QuestionItemDto
import com.m4ykey.stos.user.network.model.OwnerDto

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

}
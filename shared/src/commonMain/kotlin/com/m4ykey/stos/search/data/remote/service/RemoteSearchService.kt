package com.m4ykey.stos.search.data.remote.service

import com.m4ykey.stos.core.Filters.QUESTION_HOME_FILTER
import com.m4ykey.stos.core.Filters.TAGS_FILTER
import com.m4ykey.stos.core.model.Items
import com.m4ykey.stos.question.data.remote.model.QuestionItemDto
import com.m4ykey.stos.search.data.remote.model.TagsItemDto

interface RemoteSearchService {

    suspend fun searchQuestions(
        order : String = "desc",
        sort : String,
        site : String = "stackoverflow",
        filter : String = QUESTION_HOME_FILTER,
        page : Int,
        pageSize : Int,
        inTitle : String
    ) : Items<QuestionItemDto>

    suspend fun getTags(
        page : Int,
        pageSize : Int,
        order : String = "desc",
        sort : String = "popular",
        site : String = "stackoverflow",
        filter : String = TAGS_FILTER
    ) : Items<TagsItemDto>

}
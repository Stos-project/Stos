package com.m4ykey.stos.user

import com.m4ykey.stos.core.Items
import com.m4ykey.stos.question.network.model.QuestionItemDto
import com.m4ykey.stos.user.data.remote.model.AnswerItemDto
import com.m4ykey.stos.user.data.remote.model.OwnerDto
import com.m4ykey.stos.user.data.remote.service.RemoteUserService

class FakeRemoteUserServicePaging(
    private val questionsHasMore : Boolean = false,
    private val answersHasMore : Boolean = false,
    private val questionsError : Exception? = null,
    private val answersError : Exception? = null
) : RemoteUserService {

    var questionsPage : Int? = null
        private set

    var questionsPageSize : Int? = null
        private set

    var answersPage : Int? = null
        private set

    var answersPageSize : Int? = null
        private set

    override suspend fun getUserById(
        filter: String,
        site: String,
        id: Int
    ): Items<OwnerDto> {
        return Items(items = emptyList())
    }

    override suspend fun getUserQuestions(
        site: String,
        id: Int,
        order: String,
        sort: String,
        page: Int,
        filter: String,
        pageSize: Int
    ): Items<QuestionItemDto> {
        questionsPage = page
        questionsPageSize = pageSize

        questionsError?.let { throw it }

        return Items(
            has_more = questionsHasMore,
            items = emptyList()
        )
    }

    override suspend fun getUserAnswers(
        site: String,
        id: Int,
        order: String,
        sort: String,
        page: Int,
        pageSize: Int,
        filter: String
    ): Items<AnswerItemDto> {
        answersPage = page
        answersPageSize = pageSize

        answersError?.let { throw it }

        return Items(
            has_more = answersHasMore,
            items = emptyList()
        )
    }
}
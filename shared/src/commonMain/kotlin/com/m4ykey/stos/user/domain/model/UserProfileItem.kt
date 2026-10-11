package com.m4ykey.stos.user.domain.model

import com.m4ykey.stos.question.domain.model.QuestionItem

sealed interface UserProfileItem {
    data class Question(
        val item : QuestionItem
    ) : UserProfileItem

    data class Answer(
        val item : AnswerItem
    ) : UserProfileItem
}
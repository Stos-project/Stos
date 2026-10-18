package com.m4ykey.stos.question.domain.model

import com.m4ykey.stos.user.domain.model.Owner

data class QuestionComments (
    val owner : Owner,
    val creationDate : Int,
    val commentId : Int,
    val bodyMarkdown : String,
    val body : String,
    val score : Int
)

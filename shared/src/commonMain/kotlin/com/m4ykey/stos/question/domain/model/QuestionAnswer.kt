package com.m4ykey.stos.question.domain.model

import com.m4ykey.stos.user.domain.model.Owner

data class QuestionAnswer(
    val answerId : Int,
    val bodyMarkdown : String,
    val commentCount : Int,
    val creationDate : Int,
    val downVoteCount : Int,
    val isAccepted : Boolean,
    val owner : Owner,
    val upVoteCount : Int
)

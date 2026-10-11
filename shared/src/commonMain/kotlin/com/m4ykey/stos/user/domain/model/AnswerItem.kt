package com.m4ykey.stos.user.domain.model

data class AnswerItem(
    val answerId : Int,
    val creationDate : Int,
    val owner : Owner,
    val questionId : Int,
    val title : String
)

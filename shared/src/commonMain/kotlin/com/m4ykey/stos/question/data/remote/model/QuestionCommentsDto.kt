package com.m4ykey.stos.question.data.remote.model

import com.m4ykey.stos.user.data.remote.model.OwnerDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class QuestionCommentsDto (
    val owner : OwnerDto? = null,
    @SerialName("creation_date") val creationDate : Int? = null,
    @SerialName("comment_id") val commentId : Int? = null,
    @SerialName("body_markdown") val bodyMarkdown : String? = null,
    val body : String? = null,
    val score : Int? = null
)
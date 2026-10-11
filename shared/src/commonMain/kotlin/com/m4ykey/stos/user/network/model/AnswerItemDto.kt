package com.m4ykey.stos.user.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AnswerItemDto(
    @SerialName("answer_id") val answerId: Int? = null,
    @SerialName("creation_date") val creationDate: Int? = null,
    val owner: OwnerDto? = null,
    @SerialName("question_id") val questionId: Int? = null,
    val title: String? = null
)
package com.m4ykey.stos.question.network.model

import com.m4ykey.stos.user.data.remote.model.OwnerDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class QuestionAnswerDto(
    @SerialName("answer_id") val answerId: Int? = null,
    @SerialName("body_markdown") val bodyMarkdown: String? = null,
    @SerialName("comment_count") val commentCount: Int? = null,
    @SerialName("creation_date") val creationDate: Int? = null,
    @SerialName("down_vote_count") val downVoteCount: Int? = null,
    @SerialName("is_accepted") val isAccepted: Boolean = false,
    val owner: OwnerDto? = null,
    @SerialName("up_vote_count") val upVoteCount: Int? = null
)
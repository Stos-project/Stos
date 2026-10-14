package com.m4ykey.stos.question.mapper

import com.m4ykey.stos.question.domain.model.QuestionAnswer
import com.m4ykey.stos.question.domain.model.QuestionDetail
import com.m4ykey.stos.question.domain.model.QuestionItem
import com.m4ykey.stos.question.network.model.QuestionAnswerDto
import com.m4ykey.stos.question.network.model.QuestionDetailDto
import com.m4ykey.stos.question.network.model.QuestionItemDto
import com.m4ykey.stos.user.domain.model.Owner
import com.m4ykey.stos.user.data.mapper.toOwner

fun QuestionDetailDto.toQuestionDetail() : QuestionDetail {
    return QuestionDetail(
        answerCount = answerCount ?: 0,
        title = title.orEmpty(),
        bodyMarkdown = bodyMarkdown.orEmpty(),
        commentCount = commentCount ?: 0,
        link = link.orEmpty(),
        viewCount = viewCount ?: 0,
        upVoteCount = upVoteCount ?: 0,
        questionId = questionId ?: 0,
        downVoteCount = downVoteCount ?: 0,
        creationDate = creationDate ?: 0,
        lastActivityDate = lastActivityDate ?: 0,
        owner = owner?.toOwner() ?: Owner.EMPTY,
        tags = tags ?: emptyList(),
        lastEditor = lastEditor?.toOwner()
    )
}

fun QuestionItemDto.toQuestionItem() : QuestionItem {
    return QuestionItem(
        bodyMarkdown = bodyMarkdown.orEmpty(),
        creationDate = creationDate ?: 0,
        title = title.orEmpty(),
        viewCount = viewCount ?: 0,
        upVoteCount = upVoteCount ?: 0,
        downVoteCount = downVoteCount ?: 0,
        questionId = questionId ?: 0,
        closedDate = closedDate ?: 0,
        closedReason = closedReason.orEmpty(),
        answerCount = answerCount ?: 0,
        owner = owner?.toOwner() ?: Owner.EMPTY
    )
}

fun QuestionAnswerDto.toQuestionAnswer() : QuestionAnswer {
    return QuestionAnswer(
        upVoteCount = upVoteCount ?: 0,
        downVoteCount = downVoteCount ?: 0,
        answerId = answerId ?: 0,
        commentCount = commentCount ?: 0,
        creationDate = creationDate ?: 0,
        isAccepted = isAccepted,
        bodyMarkdown = bodyMarkdown.orEmpty(),
        owner = owner?.toOwner() ?: Owner.EMPTY
    )
}
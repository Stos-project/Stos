package com.m4ykey.stos.question.presentation.state

import com.m4ykey.stos.question.domain.model.RelatedQuestionSort

data class QuestionRelatedState(
    val sort: RelatedQuestionSort = RelatedQuestionSort.ACTIVITY
)


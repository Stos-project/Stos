package com.m4ykey.stos.question.domain.model

import com.m4ykey.stos.core.model.SortOption
import org.jetbrains.compose.resources.StringResource
import stos.shared.generated.resources.Res
import stos.shared.generated.resources.activity
import stos.shared.generated.resources.creation
import stos.shared.generated.resources.hot
import stos.shared.generated.resources.month
import stos.shared.generated.resources.votes
import stos.shared.generated.resources.week

enum class QuestionSort(override val labelRes: StringResource) : SortOption {
    HOT(Res.string.hot),
    ACTIVITY(Res.string.activity),
    VOTES(Res.string.votes),
    CREATION(Res.string.creation),
    WEEK(Res.string.week),
    MONTH(Res.string.month)
}
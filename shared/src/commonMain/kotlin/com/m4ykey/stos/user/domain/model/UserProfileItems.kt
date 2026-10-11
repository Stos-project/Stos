package com.m4ykey.stos.user.domain.model

import com.m4ykey.stos.core.SortOption
import org.jetbrains.compose.resources.StringResource
import stos.shared.generated.resources.Res
import stos.shared.generated.resources.answers
import stos.shared.generated.resources.questions

enum class UserProfileItems(override val labelRes: StringResource) : SortOption {
    QUESTIONS(Res.string.questions),
    ANSWERS(Res.string.answers)
}
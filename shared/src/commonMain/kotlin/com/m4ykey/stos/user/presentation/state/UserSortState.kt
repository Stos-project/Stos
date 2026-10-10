package com.m4ykey.stos.user.presentation.state

import com.m4ykey.stos.user.domain.model.UserProfileItems

data class UserSortState(
    val sort : UserProfileItems = UserProfileItems.QUESTIONS
)

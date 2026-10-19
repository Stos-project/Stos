package com.m4ykey.stos.question.domain.model

import org.jetbrains.compose.resources.DrawableResource
import org.jetbrains.compose.resources.StringResource

data class DrawerItem(
    val onClick : () -> Unit,
    val title : StringResource,
    val icon : DrawableResource
)

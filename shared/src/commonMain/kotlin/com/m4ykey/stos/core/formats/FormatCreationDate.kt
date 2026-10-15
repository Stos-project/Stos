package com.m4ykey.stos.core.formats

import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.stringResource
import stos.shared.generated.resources.Res
import stos.shared.generated.resources.days
import stos.shared.generated.resources.h
import stos.shared.generated.resources.min
import stos.shared.generated.resources.sec
import kotlin.time.Clock
import kotlin.time.Instant

@Composable
fun formatCreationDate(date : Long) : String {
    val now = Clock.System.now()
    val creationDate = Instant.fromEpochSeconds(date)
    val difference = now - creationDate

    val days = difference.inWholeDays
    val hours = difference.inWholeHours % 24
    val minutes = difference.inWholeMinutes % 60
    val seconds = difference.inWholeSeconds % 60

    return when {
        days > 0 -> "$days ${stringResource(Res.string.days)}"
        hours > 0 -> "$hours ${stringResource(Res.string.h)}"
        minutes > 0 -> "$minutes ${stringResource(Res.string.min)}"
        else -> "$seconds ${stringResource(Res.string.sec)}"
    }
}
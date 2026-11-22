package com.m4ykey.stos.core.network

import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.time.Duration.Companion.seconds
import kotlin.time.TimeMark
import kotlin.time.TimeSource

object BackoffGate {

    private val blockedUntil = MutableStateFlow<TimeMark?>(null)

    fun blockFor(seconds : Int) {
        if (seconds <= 0) return

        val newMark = TimeSource.Monotonic.markNow() + seconds.seconds
        val current = blockedUntil.value

        val currentStillWins = current != null &&
                !current.hasPassedNow() &&
                current.elapsedNow() <= newMark.elapsedNow()

        if (!currentStillWins) {
            blockedUntil.value = newMark
        }
    }

    fun remainingSeconds() : Long {
        val mark = blockedUntil.value ?: return 0
        return (-mark.elapsedNow()).inWholeSeconds.coerceAtLeast(0)
    }
}
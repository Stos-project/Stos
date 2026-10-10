package com.m4ykey.stos.core.ui

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import stos.shared.generated.resources.Res
import stos.shared.generated.resources.ic_arrow_up

@Composable
fun FloatingActionArrowUp(
    coroutineScope : CoroutineScope,
    listState : LazyListState
) {
    FloatingActionButton(
        onClick = {
            coroutineScope.launch {
                listState.animateScrollToItem(0)
            }
        },
        content = {
            Icon(
                contentDescription = null,
                painter = painterResource(Res.drawable.ic_arrow_up),
                modifier = Modifier.size(24.dp)
            )
        }
    )
}
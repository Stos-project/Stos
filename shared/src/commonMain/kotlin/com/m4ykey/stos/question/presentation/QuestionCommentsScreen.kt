package com.m4ykey.stos.question.presentation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.m4ykey.stos.core.ui.ActionButton
import com.m4ykey.stos.core.ui.AppScaffold
import stos.shared.generated.resources.Res
import stos.shared.generated.resources.back
import stos.shared.generated.resources.ic_arrow_back

@Composable
fun QuestionCommentsScreen(
    modifier: Modifier = Modifier,
    onBack : () -> Unit,
    questionId : Int
){
    AppScaffold(
        content = {
        },
        navigation = {
            ActionButton(
                icon = Res.drawable.ic_arrow_back,
                text = Res.string.back,
                onClick = onBack
            )
        }
    )
}
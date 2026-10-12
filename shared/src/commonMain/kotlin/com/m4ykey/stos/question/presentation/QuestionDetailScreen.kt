package com.m4ykey.stos.question.presentation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.m4ykey.stos.core.network.openBrowser
import com.m4ykey.stos.core.ui.ActionButton
import com.m4ykey.stos.core.ui.AppScaffold
import com.m4ykey.stos.core.ui.ErrorItem
import com.m4ykey.stos.core.ui.LoadingItem
import com.m4ykey.stos.question.presentation.state.QuestionDetailState
import com.m4ykey.text_markdown.TextMarkdown
import org.koin.compose.viewmodel.koinViewModel
import stos.shared.generated.resources.Res
import stos.shared.generated.resources.back
import stos.shared.generated.resources.ic_arrow_back
import stos.shared.generated.resources.ic_category
import stos.shared.generated.resources.ic_link
import stos.shared.generated.resources.link
import stos.shared.generated.resources.related_questions

@Composable
fun QuestionDetailScreen(
    modifier : Modifier = Modifier,
    onBack : () -> Unit,
    onRelatedQuestions : (Int) -> Unit,
    questionId : Int,
    viewModel: QuestionViewModel = koinViewModel()
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    val state by viewModel.questionDetailState.collectAsStateWithLifecycle()

    val listState = rememberLazyListState()

    LaunchedEffect(questionId) {
        viewModel.getQuestionDetail(questionId)
    }

    AppScaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        scrollBehavior = scrollBehavior,
        content = {
            QuestionDetailContent(
                listState = listState,
                state = state,
                onRetry = {viewModel.onRetryDetailState(questionId)}
            )
        },
        navigation = {
            ActionButton(
                icon = Res.drawable.ic_arrow_back,
                text = Res.string.back,
                onClick = onBack
            )
        },
        actions = {
            ActionButton(
                icon = Res.drawable.ic_category,
                text = Res.string.related_questions,
                onClick = {
                    state.item?.let { id ->
                        onRelatedQuestions(id.questionId)
                    }
                }
            )
        }
    )
}

@Composable
fun QuestionDetailContent(
    state : QuestionDetailState,
    onRetry : () -> Unit,
    listState : LazyListState
) {
    when {
        state.isLoading -> {
            LoadingItem()
        }
        state.error != null -> {
            ErrorItem(message = state.error, onRetry = onRetry )
        }
        else -> {
            val item = state.item

            if (item != null) {
                LazyColumn(
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxSize(),
                    state = listState
                ) {
                    item {
                        TextMarkdown(
                            alignment = Alignment.TopStart,
                            text = item.title,
                            fontSize = 24.sp
                        )
                    }
                    item {
                        TextMarkdown(
                            alignment = Alignment.TopStart,
                            text = item.bodyMarkdown
                        )
                    }
                }
            }
        }
    }
}
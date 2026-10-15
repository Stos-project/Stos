package com.m4ykey.stos.question.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.m4ykey.stos.core.formats.formatReputation
import com.m4ykey.stos.core.network.openBrowser
import com.m4ykey.stos.core.paging.PagingAppendState
import com.m4ykey.stos.core.ui.ActionButton
import com.m4ykey.stos.core.ui.AppScaffold
import com.m4ykey.stos.core.ui.ErrorItem
import com.m4ykey.stos.core.ui.LoadingItem
import com.m4ykey.stos.question.domain.model.BadgeCounts
import com.m4ykey.stos.question.domain.model.QuestionAnswer
import com.m4ykey.stos.question.presentation.components.AnswerItem
import com.m4ykey.stos.question.presentation.components.QuestionInfoRow
import com.m4ykey.stos.question.presentation.components.TagListWrap
import com.m4ykey.stos.question.presentation.state.QuestionDetailState
import com.m4ykey.stos.user.presentation.components.BadgeCountsRow
import com.m4ykey.stos.user.presentation.components.UserCard
import com.m4ykey.text_markdown.TextMarkdown
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import stos.shared.generated.resources.Res
import stos.shared.generated.resources.answers
import stos.shared.generated.resources.back
import stos.shared.generated.resources.ic_arrow_back
import stos.shared.generated.resources.ic_category
import stos.shared.generated.resources.ic_link
import stos.shared.generated.resources.last_editor
import stos.shared.generated.resources.link
import stos.shared.generated.resources.no_answers
import stos.shared.generated.resources.related_questions

@Composable
fun QuestionDetailScreen(
    modifier : Modifier = Modifier,
    onBack : () -> Unit,
    onRelatedQuestions : (Int) -> Unit,
    questionId : Int,
    viewModel: QuestionViewModel = koinViewModel(),
    onUserClick: (Int) -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()

    val state by viewModel.questionDetailState.collectAsStateWithLifecycle()
    val listState = rememberLazyListState()
    val answers = viewModel.getQuestionAnswers().collectAsLazyPagingItems()

    LaunchedEffect(questionId) {
        viewModel.getQuestionDetail(questionId)
        viewModel.setAnswersQuestionId(questionId)
    }

    AppScaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        scrollBehavior = scrollBehavior,
        content = {
            QuestionDetailContent(
                listState = listState,
                state = state,
                answers = answers,
                onRetry = { viewModel.onRetryDetailState(questionId) },
                onUserClick = onUserClick
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
                    state.item?.let { question ->
                        onRelatedQuestions(question.questionId)
                    }
                }
            )
            ActionButton(
                icon = Res.drawable.ic_link,
                text = Res.string.link,
                onClick = {
                    state.item?.let { url ->
                        openBrowser(url.link)
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
    listState : LazyListState,
    answers : LazyPagingItems<QuestionAnswer>,
    onUserClick: (Int) -> Unit
) {
    when {
        state.isLoading -> {
            LoadingItem()
        }
        state.error != null -> {
            ErrorItem(message = state.error, onRetry = onRetry )
        }
        else -> {
            val question = state.item

            if (question != null) {
                LazyColumn(
                    modifier = Modifier
                        .padding(10.dp)
                        .fillMaxSize(),
                    state = listState,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        TextMarkdown(
                            alignment = Alignment.TopStart,
                            text = question.title,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    item {
                        TextMarkdown(
                            alignment = Alignment.TopStart,
                            text = question.bodyMarkdown
                        )
                    }
                    question.lastEditor?.let { editor ->
                        item {
                            Text(
                                text = "${stringResource(Res.string.last_editor)}: ${editor.displayName}",
                                fontStyle = FontStyle.Italic,
                                fontSize = 15.sp
                            )
                        }
                    }
                    item {
                        TagListWrap(
                            tags = question.tags,
                            onTagClick = { tag -> }
                        )
                    }
                    item {
                        QuestionInfoRow(
                            downVoteCount = question.downVoteCount,
                            upVoteCount = question.upVoteCount,
                            answerCount = question.answerCount,
                            viewCount = question.viewCount
                        )
                    }
                    item {
                        DisplayUser(
                            profileImage = question.owner.profileImage,
                            displayName = question.owner.displayName,
                            reputation = question.owner.reputation,
                            badgeCounts = question.owner.badgeCounts,
                            userId = question.owner.userId,
                            onUserClick = onUserClick
                        )
                    }
                    item {
                        Text(
                            text = "${stringResource(Res.string.answers)}: ${question.answerCount}",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    when (val refresh = answers.loadState.refresh) {
                        is LoadState.Loading -> {
                            if (answers.itemCount == 0) {
                                item { LoadingItem() }
                            }
                        }
                        is LoadState.Error -> {
                            item {
                                ErrorItem(
                                    onRetry = { answers.retry() },
                                    message = refresh.error.message ?: "Loading error"
                                )
                            }
                        }
                        is LoadState.NotLoading -> {
                            if (answers.itemCount == 0) {
                                item {
                                    Text(
                                        modifier = Modifier.fillMaxWidth(),
                                        text = stringResource(Res.string.no_answers),
                                        fontSize = 16.sp,
                                        textAlign = TextAlign.Center
                                    )
                                }
                            }
                        }
                    }

                    items(
                        count = answers.itemCount,
                        key = answers.itemKey { it.answerId }
                    ) { index ->
                        val answer = answers[index]

                        answer?.let { item ->
                            AnswerItem(
                                answer = item,
                                onUserClick = onUserClick
                            )
                        }
                    }

                    item {
                        PagingAppendState(items = answers)
                    }
                }
            }
        }
    }
}

@Composable
fun DisplayUser(
    profileImage : String,
    displayName : String,
    reputation : Int,
    badgeCounts: BadgeCounts,
    onUserClick: (Int) -> Unit,
    userId : Int
) {
    Row(
        modifier = Modifier
            .clickable { onUserClick(userId) }
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(10.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        UserCard(profileImage = profileImage)
        Column(modifier = Modifier.fillMaxSize()) {
            TextMarkdown(
                text = displayName,
                alignment = Alignment.TopStart
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text(
                    text = formatReputation(reputation),
                    fontSize = 16.sp
                )
                BadgeCountsRow(
                    badgeCounts = badgeCounts
                )
            }
        }
    }
}
package com.m4ykey.stos.question.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.m4ykey.stos.core.formatCreationDate
import com.m4ykey.stos.core.formatReputation
import com.m4ykey.stos.core.network.openBrowser
import com.m4ykey.stos.core.ui.ActionButton
import com.m4ykey.stos.core.ui.AppScaffold
import com.m4ykey.stos.core.ui.ErrorItem
import com.m4ykey.stos.core.ui.LoadingItem
import com.m4ykey.stos.question.domain.model.BadgeCounts
import com.m4ykey.stos.question.presentation.components.QuestionInfoRow
import com.m4ykey.stos.question.presentation.state.QuestionDetailState
import com.m4ykey.stos.user.presentation.components.BadgeCountsRow
import com.m4ykey.stos.user.presentation.components.UserCard
import com.m4ykey.text_markdown.TextMarkdown
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import stos.shared.generated.resources.Res
import stos.shared.generated.resources.answers
import stos.shared.generated.resources.asked
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
                    state = listState,
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        TextMarkdown(
                            alignment = Alignment.TopStart,
                            text = item.title,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    item {
                        TextMarkdown(
                            alignment = Alignment.TopStart,
                            text = item.bodyMarkdown
                        )
                    }
                    item {
                        QuestionInfoRow(
                            downVoteCount = item.downVoteCount,
                            upVoteCount = item.upVoteCount,
                            answerCount = item.answerCount,
                            viewCount = item.viewCount
                        )
                    }
                    item {
                        Text(
                            text = "${stringResource(Res.string.asked)} ${formatCreationDate(item.creationDate.toLong())}",
                            fontSize = 14.sp
                        )
                    }
                    item {
                        DisplayUser(
                            profileImage = item.owner.profileImage,
                            displayName = item.owner.displayName,
                            reputation = item.owner.reputation,
                            badgeCounts = item.owner.badgeCounts
                        )
                    }
                    item {
                        Text(
                            text = "${stringResource(Res.string.answers)}: ${item.answerCount}"
                        )
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
    badgeCounts: BadgeCounts
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
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
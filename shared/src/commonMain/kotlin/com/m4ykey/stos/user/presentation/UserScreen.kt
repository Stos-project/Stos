package com.m4ykey.stos.user.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.LazyPagingItems
import androidx.paging.compose.collectAsLazyPagingItems
import com.m4ykey.stos.core.network.openBrowser
import com.m4ykey.stos.core.ui.ActionButton
import com.m4ykey.stos.core.ui.AppScaffold
import com.m4ykey.stos.core.ui.ErrorItem
import com.m4ykey.stos.core.ui.FloatingActionArrowUp
import com.m4ykey.stos.core.ui.LoadingItem
import com.m4ykey.stos.question.domain.model.QuestionItem
import com.m4ykey.stos.question.presentation.components.ChipList
import com.m4ykey.stos.question.presentation.components.QuestionListItem
import com.m4ykey.stos.user.domain.model.UserProfileItems
import com.m4ykey.stos.user.presentation.components.UserProfile
import com.m4ykey.stos.user.presentation.state.UserState
import org.koin.compose.viewmodel.koinViewModel
import stos.shared.generated.resources.Res
import stos.shared.generated.resources.back
import stos.shared.generated.resources.ic_arrow_back
import stos.shared.generated.resources.ic_link
import stos.shared.generated.resources.link

@Composable
fun UserScreen(
    modifier : Modifier = Modifier,
    userId : Int,
    onBack : () -> Unit,
    viewModel: UserViewModel = koinViewModel(),
    onQuestionClick : (Int) -> Unit
) {
    val listState = rememberLazyListState()

    val state by viewModel.userState.collectAsStateWithLifecycle()
    val questionState = viewModel.userQuestionItems.collectAsLazyPagingItems()
    val viewState by viewModel.userSortState.collectAsStateWithLifecycle()

    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val coroutineScope = rememberCoroutineScope()

    val showScrollByPosition by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex > 20
        }
    }

    LaunchedEffect(userId) {
        viewModel.getUserById(userId)
        viewModel.loadUserItems(userId)
    }

    AppScaffold(
        scrollBehavior = scrollBehavior,
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        floatingActionButton = {
            AnimatedVisibility(
                visible = showScrollByPosition,
                enter = slideInHorizontally { it },
                exit = slideOutHorizontally { it }
            ) {
                FloatingActionArrowUp(
                    listState = listState,
                    coroutineScope = coroutineScope
                )
            }
        },
        content = {
            UserScreenContent(
                state = state,
                listState = listState,
                onRetry = { viewModel.onRetryUserState(userId) },
                sort = viewState.sort,
                onSortChange = viewModel::updateSort,
                questionState = questionState,
                onQuestionClick = onQuestionClick
            )
        },
        navigation = {
            ActionButton(
                onClick = onBack,
                text = Res.string.back,
                icon = Res.drawable.ic_arrow_back
            )
        },
        actions = {
            ActionButton(
                icon = Res.drawable.ic_link,
                text = Res.string.link,
                onClick = {
                    state.user?.let { user ->
                        openBrowser(user.link)
                    }
                }
            )
        }
    )
}

@Composable
fun UserScreenContent(
    modifier : Modifier = Modifier,
    state : UserState,
    listState : LazyListState,
    onRetry : () -> Unit,
    sort : UserProfileItems,
    questionState : LazyPagingItems<QuestionItem>,
    onSortChange : (UserProfileItems) -> Unit,
    onQuestionClick : (Int) -> Unit
) {
    when {
        state.isLoading -> LoadingItem()
        state.error != null -> ErrorItem(
            message = state.error,
            onRetry = { onRetry() }
        )
        else -> {
            val item = state.user

            if (item != null) {
                LazyColumn(
                    state = listState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(10.dp)
                ) {
                    item {
                        UserProfile(
                            profileImage = item.profileImage,
                            displayName = item.displayName,
                            reputation = item.reputation,
                            badgeCounts = item.badgeCounts
                        )
                    }
                    item {
                        Row(modifier = Modifier.fillMaxWidth()) {
                            ChipList(
                                modifier = Modifier.fillMaxWidth(),
                                availableSorts = UserProfileItems.entries,
                                selectedChip = sort,
                                onChipSelected = onSortChange
                            )
                        }
                    }
                    items(
                        key = { index -> questionState[index]?.questionId ?: index },
                        count = questionState.itemCount
                    ) { index ->
                        val question = questionState[index]

                        if (question != null) {
                            QuestionListItem(
                                onOwnerClick = {},
                                onQuestionClick = onQuestionClick,
                                item = question
                            )
                        }
                    }

                    if (questionState.loadState.append is LoadState.Loading) {
                        item {
                            LoadingItem()
                        }
                    }
                }
            }
        }
    }
}
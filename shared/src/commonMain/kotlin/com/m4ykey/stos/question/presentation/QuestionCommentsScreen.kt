package com.m4ykey.stos.question.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.m4ykey.stos.core.paging.PagingAppendState
import com.m4ykey.stos.core.ui.ActionButton
import com.m4ykey.stos.core.ui.AppScaffold
import com.m4ykey.stos.core.ui.ErrorItem
import com.m4ykey.stos.core.ui.FloatingActionArrowUp
import com.m4ykey.stos.core.ui.LoadingItem
import stos.shared.generated.resources.Res
import stos.shared.generated.resources.back
import stos.shared.generated.resources.ic_arrow_back

@Composable
fun QuestionCommentsScreen(
    modifier: Modifier = Modifier,
    onBack : () -> Unit,
    questionId : Int
){
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    val listState = rememberLazyListState()
    val coroutineScope = rememberCoroutineScope()
    val showScrollByPosition by remember {
        derivedStateOf {
            listState.firstVisibleItemIndex > 20
        }
    }
    AppScaffold(
        modifier = modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        scrollBehavior = scrollBehavior,
        content = {
        },
        navigation = {
            ActionButton(
                icon = Res.drawable.ic_arrow_back,
                text = Res.string.back,
                onClick = onBack
            )
        },
        floatingActionButton = {
            AnimatedVisibility(
                visible = showScrollByPosition,
                enter = slideInHorizontally { it },
                exit = slideOutHorizontally { it }
            ){
                FloatingActionArrowUp(
                    coroutineScope = coroutineScope,
                    listState = listState
                )
            }
        }
    )
}
@Composable
fun QuestionCommentsContent(
    modifier : Modifier = Modifier,
    viewModel : QuestionViewModel,
    onOwnerClick : (Int) -> Unit,
    listState : LazyListState,
    questionId: Int
){
    LaunchedEffect(questionId){
        viewModel.setQuestionCommentsId(questionId)
    }

    val items = viewModel.getQuestionComments().collectAsLazyPagingItems()

    when (val loadState = items.loadState.refresh){
        is LoadState.Loading -> LoadingItem()
        is LoadState.Error ->{
            ErrorItem(
                message = loadState.error.message ?: "Loading error",
                onRetry = {items.retry()}
            )
        }
        is LoadState.NotLoading ->{
            LazyColumn(
                contentPadding = PaddingValues(horizontal = 10.dp),
                state = listState,
                verticalArrangement = Arrangement.spacedBy(15.dp)
            ){
                items(
                    count = items.itemCount,
                    key = items.itemKey { it.commentId }
                ){ index ->
                    val comment = items[index]

                    comment?.let { item ->
                    }

                }
                item{
                    PagingAppendState(items = items)
                }
            }
        }
    }
}
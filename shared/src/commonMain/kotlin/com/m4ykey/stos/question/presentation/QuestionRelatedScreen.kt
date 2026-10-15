package com.m4ykey.stos.question.presentation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.m4ykey.stos.core.paging.PagingAppendState
import com.m4ykey.stos.core.ui.ActionButton
import com.m4ykey.stos.core.ui.AppScaffold
import com.m4ykey.stos.core.ui.ErrorItem
import com.m4ykey.stos.core.ui.FloatingActionArrowUp
import com.m4ykey.stos.core.ui.LoadingItem
import com.m4ykey.stos.question.domain.model.QuestionSort
import com.m4ykey.stos.question.domain.model.RelatedQuestionSort
import com.m4ykey.stos.question.presentation.components.ChipList
import com.m4ykey.stos.question.presentation.components.QuestionListItem
import org.koin.compose.viewmodel.koinViewModel
import stos.shared.generated.resources.Res
import stos.shared.generated.resources.back
import stos.shared.generated.resources.ic_arrow_back

@Composable
fun QuestionRelatedScreen(
    modifier : Modifier = Modifier,
    viewModel : QuestionViewModel = koinViewModel(),
    questionId : Int,
    onQuestionClick : (Int) -> Unit,
    onOwnerClick : (Int) -> Unit,
    onBack : () -> Unit
)
{
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
            QuestionRelatedContent(
                viewModel = viewModel,
                onQuestionClick = onQuestionClick,
                onOwnerClick = onOwnerClick,
                listState = listState,
                questionId = questionId
            )
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
            ) {
                FloatingActionArrowUp(
                    coroutineScope = coroutineScope,
                    listState = listState
                )
            }
        },
    )
}
@Composable
fun QuestionRelatedContent(
    modifier: Modifier = Modifier,
    viewModel: QuestionViewModel,
    onOwnerClick: (Int) -> Unit,
    onQuestionClick: (Int) -> Unit,
    listState: LazyListState,
    questionId: Int
){
    val items = viewModel.getRelatedQuestions(questionId).collectAsLazyPagingItems()
    val viewState by viewModel.questionRelatedState.collectAsStateWithLifecycle()
    val sort = viewState.sort

    var shouldScrollAfterRefresh by remember { mutableStateOf(false) }

    LaunchedEffect(items.loadState.refresh){
        if (shouldScrollAfterRefresh && items.loadState.refresh is LoadState.NotLoading){
            shouldScrollAfterRefresh = false
            listState.animateScrollToItem(0)
        }
    }

    Column(modifier = modifier.fillMaxSize()){
        Row(modifier = Modifier.fillMaxWidth()){
            ChipList(
                modifier = Modifier.fillMaxWidth(),
                availableSorts = RelatedQuestionSort.entries,
                selectedChip = sort,
                onChipSelected = { selectedSort ->
                    shouldScrollAfterRefresh = true
                    //viewModel.updateRelatedSort(selectedSort)
                }
            )
        }
        Spacer(modifier = Modifier.height(10.dp))

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
                        key = items.itemKey { it.questionId }
                    ) { index ->
                        val question = items[index]

                        question?.let { item ->
                            QuestionListItem(
                                onOwnerClick = onOwnerClick,
                                onQuestionClick = onQuestionClick,
                                item = item
                            )
                        }

                    }
                    item{
                        PagingAppendState(items = items)
                    }
                }
            }
        }
    }

}
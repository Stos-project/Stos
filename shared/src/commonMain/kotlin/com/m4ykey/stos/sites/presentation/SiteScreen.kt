package com.m4ykey.stos.sites.presentation

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
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.m4ykey.stos.core.paging.PagingAppendState
import com.m4ykey.stos.core.ui.ActionButton
import com.m4ykey.stos.core.ui.AppScaffold
import com.m4ykey.stos.core.ui.ErrorItem
import com.m4ykey.stos.core.ui.FloatingActionArrowUp
import com.m4ykey.stos.core.ui.LoadingItem
import com.m4ykey.stos.sites.presentation.components.SiteListItem
import org.koin.compose.viewmodel.koinViewModel
import stos.shared.generated.resources.Res
import stos.shared.generated.resources.back
import stos.shared.generated.resources.ic_arrow_back

@Composable
fun SiteScreen(
    onBack : () -> Unit,
    modifier : Modifier = Modifier,
    viewModel: SitesViewModel = koinViewModel()
) {
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
            SiteContent(
                viewModel = viewModel,
                listState = listState,
                onBack = onBack
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
                    listState = listState,
                    coroutineScope = coroutineScope
                )
            }
        }
    )
}

@Composable
fun SiteContent(
    viewModel: SitesViewModel,
    listState : LazyListState,
    onBack: () -> Unit
) {
    val items = viewModel.sites.collectAsLazyPagingItems()

    val currentSelected by viewModel.selectedSite.collectAsStateWithLifecycle()

    when (val loadState = items.loadState.refresh) {
        is LoadState.Loading -> LoadingItem()
        is LoadState.Error -> {
            ErrorItem(
                onRetry = { items.retry() },
                message = loadState.error.message ?: "Loading error"
            )
        }
        is LoadState.NotLoading -> {
            LazyColumn(
                state = listState,
                verticalArrangement = Arrangement.spacedBy(15.dp),
                contentPadding = PaddingValues(horizontal = 10.dp)
            ) {
                items(
                    count = items.itemCount,
                    key = items.itemKey { it.siteUrl }
                ) { index ->
                    val sites = items[index]

                    sites?.let { site ->
                        SiteListItem(
                            item = site,
                            onClick = {
                                viewModel.selectSite(site)
                                onBack()
                            },
                            isSelected = site.apiSiteParameter == currentSelected
                        )
                    }
                }

                item {
                    PagingAppendState(items = items)
                }
            }
        }
    }
}
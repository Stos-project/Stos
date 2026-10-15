package com.m4ykey.stos.search.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.staggeredgrid.LazyHorizontalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import androidx.paging.compose.itemKey
import com.m4ykey.stos.core.ui.ActionButton
import com.m4ykey.stos.core.ui.AppScaffold
import com.m4ykey.stos.core.ui.ErrorItem
import com.m4ykey.stos.core.ui.LoadingItem
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import org.koin.compose.viewmodel.koinViewModel
import stos.shared.generated.resources.Res
import stos.shared.generated.resources.back
import stos.shared.generated.resources.ic_arrow_back
import stos.shared.generated.resources.ic_close
import stos.shared.generated.resources.ic_search
import stos.shared.generated.resources.search
import stos.shared.generated.resources.tags

@Composable
fun SearchScreen(
    onBack : () -> Unit,
    onSearch: (String) -> Unit,
    viewModel : SearchViewModel = koinViewModel()
) {
    var value by rememberSaveable { mutableStateOf("") }

    AppScaffold(
        title = stringResource(Res.string.search),
        navigation = {
            ActionButton(
                onClick = onBack,
                icon = Res.drawable.ic_arrow_back,
                text = Res.string.back
            )
        },
        content = {
            SearchScreenContent(
                value = value,
                onValueChange = { value = it },
                onSearch = {
                    if (value.isNotBlank()) onSearch(value.trim())
                },
                viewModel = viewModel
            )
        }
    )
}

@Composable
fun SearchScreenContent(
    value : String,
    onValueChange: (String) -> Unit,
    onSearch : () -> Unit,
    viewModel: SearchViewModel
) {
    val tags = viewModel.tagFlow.collectAsLazyPagingItems()
    val lazyGridState = rememberLazyStaggeredGridState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(10.dp)
    ) {
        SearchText(
            value = value,
            onValueChange = onValueChange,
            onSearch = onSearch
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = stringResource(Res.string.tags),
            modifier = Modifier.fillMaxWidth(),
            fontSize = 20.sp,
            fontWeight = FontWeight.SemiBold
        )
        when (val loadState = tags.loadState.refresh) {
            is LoadState.Loading -> LoadingItem()
            is LoadState.Error -> {
                ErrorItem(
                    message = loadState.error.message ?: "Loading error",
                    onRetry = { tags.retry() }
                )
            }
            is LoadState.NotLoading -> {
                LazyHorizontalStaggeredGrid(
                    rows = StaggeredGridCells.Fixed(3),
                    modifier = Modifier
                        .height(150.dp)
                        .fillMaxWidth(),
                    state = lazyGridState,
                    horizontalItemSpacing = 10.dp
                ) {
                    items(
                        count = tags.itemCount,
                        key = tags.itemKey { it.name }
                    ) { index ->
                        val tag = tags[index]

                        tag?.let { item ->
                            FilterChip(
                                selected = false,
                                onClick = {},
                                label = { Text(text = item.name) },
                                modifier = Modifier.wrapContentSize(),
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun SearchText(
    modifier : Modifier = Modifier,
    onSearch : () -> Unit,
    value : String,
    onValueChange : (String) -> Unit
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    OutlinedTextField(
        modifier = modifier.fillMaxWidth(),
        value = value,
        onValueChange = onValueChange,
        leadingIcon = {
            Icon(
                contentDescription = null,
                painter = painterResource(Res.drawable.ic_search),
                modifier = Modifier.size(24.dp)
            )
        },
        singleLine = true,
        trailingIcon = {
            if (value.isNotEmpty()) {
                IconButton(onClick = { onValueChange("") }) {
                    Icon(
                        contentDescription = null,
                        painter = painterResource(Res.drawable.ic_close)
                    )
                }
            }
        },
        keyboardActions = KeyboardActions(
            onSearch = {
                keyboardController?.hide()
                onSearch()
            }
        ),
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Search),
        placeholder = { Text(stringResource(Res.string.search) + "...") }
    )
}
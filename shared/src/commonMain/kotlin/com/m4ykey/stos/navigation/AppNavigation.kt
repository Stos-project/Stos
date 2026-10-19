package com.m4ykey.stos.navigation

import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.navigation3.rememberViewModelStoreNavEntryDecorator
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.runtime.rememberSaveableStateHolderNavEntryDecorator
import androidx.navigation3.ui.NavDisplay
import androidx.savedstate.serialization.SavedStateConfiguration
import com.m4ykey.stos.question.presentation.QuestionCommentsScreen
import com.m4ykey.stos.question.presentation.QuestionDetailScreen
import com.m4ykey.stos.question.presentation.QuestionHomeScreen
import com.m4ykey.stos.question.presentation.QuestionRelatedScreen
import com.m4ykey.stos.question.presentation.QuestionTagScreen
import com.m4ykey.stos.search.presentation.SearchListScreen
import com.m4ykey.stos.search.presentation.SearchScreen
import com.m4ykey.stos.sites.presentation.SiteScreen
import com.m4ykey.stos.user.presentation.UserScreen
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.polymorphic

@Composable
fun AppNavigation(modifier : Modifier = Modifier) {

    val rootBackStack = rememberNavBackStack(
        configuration = SavedStateConfiguration {
            serializersModule = SerializersModule {
                polymorphic(NavKey::class) {
                    subclass(Route.QuestionHome::class, Route.QuestionHome.serializer())
                    subclass(Route.SearchScreen::class, Route.SearchScreen.serializer())
                    subclass(Route.SearchList::class, Route.SearchList.serializer())
                    subclass(Route.QuestionDetail::class, Route.QuestionDetail.serializer())
                    subclass(Route.UserScreen::class, Route.UserScreen.serializer())
                    subclass(Route.QuestionRelated::class, Route.QuestionRelated.serializer())
                    subclass(Route.QuestionTag::class, Route.QuestionTag.serializer())
                    subclass(Route.QuestionComments::class, Route.QuestionComments.serializer())
                    subclass(Route.Sites::class, Route.Sites.serializer())
                }
            }
        },
        Route.QuestionHome
    )

    fun navigateTo(route : Route) {
        if (rootBackStack.lastOrNull() != route) {
            rootBackStack.add(route)
        }
    }

    fun navigateBack() {
        if (rootBackStack.size > 1) {
            rootBackStack.removeAt(rootBackStack.lastIndex)
        }
    }

    NavDisplay(
        backStack = rootBackStack,
        modifier = modifier,
        entryDecorators = listOf(
            rememberSaveableStateHolderNavEntryDecorator(),
            rememberViewModelStoreNavEntryDecorator()
        ),
        transitionSpec = {
            slideInHorizontally { it } togetherWith
                    slideOutHorizontally { -it }
        },
        popTransitionSpec = {
            slideInHorizontally { -it } togetherWith
                    slideOutHorizontally { it }
        },
        entryProvider = entryProvider {
            entry<Route.QuestionHome> {
                QuestionHomeScreen(
                    onSearchClick = {
                        navigateTo(Route.SearchScreen)
                    },
                    onOwnerClick = { key ->
                        navigateTo(Route.UserScreen(key))
                    },
                    onQuestionClick = { key ->
                        navigateTo(Route.QuestionDetail(key))
                    },
                    onSiteClick = {
                        navigateTo(Route.Sites)
                    }
                )
            }
            entry<Route.SearchScreen> {
                SearchScreen(
                    onBack = ::navigateBack,
                    onSearch = { key ->
                        navigateTo(Route.SearchList(key))
                    }
                )
            }
            entry<Route.SearchList> { key ->
                SearchListScreen(
                    onBack = ::navigateBack,
                    inTitle = key.inTitle,
                    onOwnerClick = { userId ->
                        navigateTo(Route.UserScreen(userId))
                    },
                    onQuestionClick = { questionId ->
                        navigateTo(Route.QuestionDetail(questionId))
                    }
                )
            }
            entry<Route.QuestionDetail> { key ->
                QuestionDetailScreen(
                    onBack = ::navigateBack,
                    questionId = key.questionId,
                    onRelatedQuestionsClick = { questionId ->
                        navigateTo(Route.QuestionRelated(questionId))
                    },
                    onUserClick = { userId ->
                        navigateTo(Route.UserScreen(userId))
                    },
                    onTagClick = { tag ->
                        navigateTo(Route.QuestionTag(tag))
                    },
                    onCommentsClick = { id ->
                        navigateTo(Route.QuestionComments(id))
                    }
                )
            }
            entry<Route.UserScreen> { key ->
                UserScreen(
                    userId = key.userId,
                    onBack = ::navigateBack,
                    onQuestionClick = { item ->
                        navigateTo(Route.QuestionDetail(item))
                    }
                )
            }
            entry<Route.QuestionRelated> { key ->
                QuestionRelatedScreen(
                    questionId = key.questionId,
                    onQuestionClick = { item ->
                        navigateTo(Route.QuestionDetail(item))
                    },
                    onOwnerClick = { userId ->
                        navigateTo(Route.UserScreen(userId))
                    },
                    onBack = ::navigateBack
                )
            }
            entry<Route.QuestionTag> { key ->
                QuestionTagScreen(
                    tagged = key.tag,
                    onOwnerClick = { userId ->
                        navigateTo(Route.UserScreen(userId))
                    },
                    onQuestionClick = { item ->
                        navigateTo(Route.QuestionDetail(item))
                    },
                    onBack = ::navigateBack
                )
            }
            entry <Route.QuestionComments> { key ->
                QuestionCommentsScreen(
                    onBack = ::navigateBack,
                    questionId = key.id,
                    onOwnerClick = { userId ->
                        navigateTo(Route.UserScreen(userId))
                    }
            entry<Route.Sites> {
                SiteScreen(
                    onBack = ::navigateBack
                )
            }
        }
    )
}
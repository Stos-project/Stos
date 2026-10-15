@file:OptIn(ExperimentalCoroutinesApi::class)

package com.m4ykey.stos.user

import com.m4ykey.stos.core.network.ApiException
import com.m4ykey.stos.core.network.ApiResult
import com.m4ykey.stos.user.domain.model.Owner
import com.m4ykey.stos.user.domain.model.UserProfileItems
import com.m4ykey.stos.user.presentation.UserViewModel
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceTimeBy
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.time.Duration.Companion.milliseconds

class UserViewModelTest {

    private lateinit var viewModel : UserViewModel
    private lateinit var repository : FakeUserRepository

    private fun createViewModel() {
        repository = FakeUserRepository()
        viewModel = UserViewModel(repository)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `getUserById updates state on success`() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        createViewModel()

        val expectedUser = Owner.EMPTY
        repository.userResult = flowOf(ApiResult.Success(expectedUser))

        viewModel.getUserById(123)
        advanceUntilIdle()

        assertFalse(viewModel.userState.value.isLoading)
        assertEquals(expectedUser, viewModel.userState.value.user)
        assertEquals(null, viewModel.userState.value.error)
    }

    @Test
    fun `getUserById updates state on failure`() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        createViewModel()

        val exception = ApiException.UnknownError(
            cause = IllegalStateException("Network error")
        )

        repository.userResult = flowOf(ApiResult.Failure(exception))

        viewModel.getUserById(123)
        advanceUntilIdle()

        assertFalse(viewModel.userState.value.isLoading)
        assertEquals(exception.message, viewModel.userState.value.error)
    }

    @Test
    fun `getUserById handles flow exception`() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        createViewModel()

        repository.userResult = flow { throw IllegalStateException("Flow failed") }

        viewModel.getUserById(123)
        advanceUntilIdle()

        assertFalse(viewModel.userState.value.isLoading)
        assertEquals("Flow failed", viewModel.userState.value.error)
    }

    @Test
    fun `initial state has default values`() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        createViewModel()

        assertFalse(viewModel.userState.value.isLoading)
        assertEquals(null, viewModel.userState.value.error)
        assertEquals(null, viewModel.userState.value.user)

        assertEquals(
            UserProfileItems.QUESTIONS,
            viewModel.userSortState.value.sort
        )
    }

    @Test
    fun `getUserById passes user id to repository`() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        createViewModel()

        viewModel.getUserById(123)
        advanceUntilIdle()

        assertEquals(1, repository.getUserByIdCalls)
        assertEquals(123, repository.lastUserId)
    }

    @Test
    fun `onRetryUserState requests user again`() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        createViewModel()

        viewModel.getUserById(123)
        advanceUntilIdle()

        viewModel.onRetryUserState(123)
        advanceUntilIdle()

        assertEquals(2, repository.getUserByIdCalls)
        assertEquals(123, repository.lastUserId)
    }

    @Test
    fun `getUserById sets loading while request is pending`() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        createViewModel()

        repository.userResult = flow {
            delay(1_000.milliseconds)
            emit(ApiResult.Success(Owner.EMPTY))
        }

        viewModel.getUserById(123)
        runCurrent()

        assertTrue(viewModel.userState.value.isLoading)
        assertEquals(null, viewModel.userState.value.error)

        advanceTimeBy(1_000.milliseconds)
        advanceUntilIdle()

        assertFalse(viewModel.userState.value.isLoading)
        assertEquals(Owner.EMPTY, viewModel.userState.value.user)
    }

    @Test
    fun `updateSort changes selected sort`() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        createViewModel()

        viewModel.updateSort(UserProfileItems.ANSWERS)

        assertEquals(
            UserProfileItems.ANSWERS,
            viewModel.userSortState.value.sort
        )
    }

    @Test
    fun `does not load items before user id is provided`() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        createViewModel()

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.userQuestionItems.collect()
        }

        advanceUntilIdle()

        assertEquals(0, repository.getUserQuestionsCalls)
        assertEquals(0, repository.getUserAnswersCalls)
    }

    @Test
    fun `loads questions for selected user`() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        createViewModel()

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.userQuestionItems.collect()
        }

        viewModel.loadUserItems(123)
        advanceUntilIdle()

        assertEquals(1, repository.getUserQuestionsCalls)
        assertEquals(123, repository.lastQuestionUserId)
        assertEquals(0, repository.getUserAnswersCalls)
    }

    @Test
    fun `loads answers for sort changes`() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        createViewModel()

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.userQuestionItems.collect()
        }

        viewModel.loadUserItems(123)
        advanceUntilIdle()

        viewModel.updateSort(UserProfileItems.ANSWERS)
        advanceUntilIdle()

        assertEquals(1, repository.getUserQuestionsCalls)
        assertEquals(1, repository.getUserAnswersCalls)
        assertEquals(123, repository.lastAnswersUserId)
    }

    @Test
    fun `loads questions for newly selected user`() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        createViewModel()

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.userQuestionItems.collect()
        }

        viewModel.loadUserItems(123)
        advanceUntilIdle()

        viewModel.loadUserItems(456)
        advanceUntilIdle()

        assertEquals(2, repository.getUserQuestionsCalls)
        assertEquals(456, repository.lastQuestionUserId)
    }

    @Test
    fun `does not reload questions for same user id`() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        createViewModel()

        backgroundScope.launch(UnconfinedTestDispatcher(testScheduler)) {
            viewModel.userQuestionItems.collect()
        }

        viewModel.loadUserItems(123)
        advanceUntilIdle()

        viewModel.loadUserItems(123)
        advanceUntilIdle()

        assertEquals(1, repository.getUserQuestionsCalls)
    }

}
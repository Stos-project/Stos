@file:OptIn(ExperimentalCoroutinesApi::class)

package com.m4ykey.stos.user

import com.m4ykey.stos.core.network.ApiException
import com.m4ykey.stos.core.network.ApiResult
import com.m4ykey.stos.user.domain.model.Owner
import com.m4ykey.stos.user.presentation.UserViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse

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
    fun `loadUserItems updates selected user id`() = runTest {
        Dispatchers.setMain(StandardTestDispatcher(testScheduler))
        createViewModel()

        viewModel.loadUserItems(123)
    }

}
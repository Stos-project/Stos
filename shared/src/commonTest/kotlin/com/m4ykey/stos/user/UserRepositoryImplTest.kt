package com.m4ykey.stos.user

import com.m4ykey.stos.core.model.Items
import com.m4ykey.stos.core.network.ApiResult
import com.m4ykey.stos.question.data.remote.model.QuestionItemDto
import com.m4ykey.stos.user.data.mapper.toOwner
import com.m4ykey.stos.user.data.remote.model.AnswerItemDto
import com.m4ykey.stos.user.data.remote.model.OwnerDto
import com.m4ykey.stos.user.data.remote.service.RemoteUserService
import com.m4ykey.stos.user.data.repository.UserRepositoryImpl
import com.m4ykey.stos.user.domain.model.Owner
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertTrue

class UserRepositoryImplTest {

    private val userDto = OwnerDto(
        displayName = "Test User",
        link = "https://stackoverflow.com/users/123",
        profileImage = "https://example.com/avatar.png",
        reputation = 213,
        userId = 123
    )

    private class FakeRemoteUserService(
        var userResponse : Items<OwnerDto> = Items(emptyList()),
        var userError : Exception? = null
    ) : RemoteUserService {

        override suspend fun getUserById(
            filter: String,
            id: Int
        ): Items<OwnerDto> {
            userError?.let { throw it }
            return userResponse
        }

        override suspend fun getUserQuestions(
            id: Int,
            order: String,
            sort: String,
            page: Int,
            filter: String,
            pageSize: Int
        ): Items<QuestionItemDto> {
            return Items(emptyList())
        }

        override suspend fun getUserAnswers(
            id: Int,
            order: String,
            sort: String,
            page: Int,
            pageSize: Int,
            filter: String
        ): Items<AnswerItemDto> {
            return Items(emptyList())
        }
    }

    @Test
    fun `getUserById emits mapped user on success`() = runTest {
        val service = FakeRemoteUserService(
            userResponse = Items(listOf(userDto))
        )
        val repository = UserRepositoryImpl(service)

        val results = repository.getUserById(123).toList()

        assertEquals(1, results.size)

        val result = assertIs<ApiResult.Success<Owner>>(results.first())
        assertEquals(userDto.toOwner(), result.data)
        assertEquals(123, result.data.userId)
        assertEquals("Test User", result.data.displayName)
    }

    @Test
    fun `getUserById emits nothing when response is empty`() = runTest {
        val service = FakeRemoteUserService(
            userResponse = Items(emptyList())
        )
        val repository = UserRepositoryImpl(service)

        val results = repository.getUserById(123).toList()

        assertTrue(results.isEmpty())
    }

    @Test
    fun `getUserById emits failure when service throws`() = runTest {
        val service = FakeRemoteUserService(
            userError = IllegalStateException("Test error")
        )
        val repository = UserRepositoryImpl(service)

        val results = repository.getUserById(123).toList()

        assertEquals(1, results.size)
        assertIs<ApiResult.Failure>(results.first())
    }

}
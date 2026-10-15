package com.m4ykey.stos.user

import androidx.paging.PagingSource
import com.m4ykey.stos.core.network.ApiException
import com.m4ykey.stos.user.data.remote.paging.UserAnswerPagingSource
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertIs
import kotlin.test.assertNull

class UserAnswerPagingSourceTest {

    @Test
    fun `load returns next key when more pages exist`() = runTest {
        val service = FakeRemoteUserServicePaging(
            answersHasMore = true
        )
        val source = UserAnswerPagingSource(
            service = service,
            id = 123
        )

        val result = source.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 20,
                placeholdersEnabled = false
            )
        )

        assertIs<PagingSource.LoadResult.Page<Int, *>>(result)
        assertEquals(2, (result as PagingSource.LoadResult.Page).nextKey)
        assertNull(result.prevKey)
        assertEquals(1, service.answersPage)
        assertEquals(20, service.answersPageSize)
    }

    @Test
    fun `load returns null next key when no more pages exist`() = runTest {
        val service = FakeRemoteUserServicePaging(
            answersHasMore = false
        )
        val source = UserAnswerPagingSource(service, 123)

        val result = source.load(
            PagingSource.LoadParams.Refresh(
                key = 1,
                loadSize = 10,
                placeholdersEnabled = false
            )
        )

        assertIs<PagingSource.LoadResult.Page<Int, *>>(result)
        assertNull((result as PagingSource.LoadResult.Page).nextKey)
    }

    @Test
    fun `load increments page key`() = runTest {
        val service = FakeRemoteUserServicePaging(
            answersHasMore = true
        )
        val source = UserAnswerPagingSource(service, 123)

        val result = source.load(
            PagingSource.LoadParams.Refresh(
                key = 3,
                loadSize = 10,
                placeholdersEnabled = false
            )
        )

        assertIs<PagingSource.LoadResult.Page<Int, *>>(result)
        assertEquals(4, (result as PagingSource.LoadResult.Page).nextKey)
        assertEquals(3, service.answersPage)
        assertEquals(2, result.prevKey)
    }

    @Test
    fun `load returns error when service fails`() = runTest {
        val service = FakeRemoteUserServicePaging(
            answersError = IllegalStateException("Network error")
        )

        val source = UserAnswerPagingSource(service, 123)

        val result = source.load(
            PagingSource.LoadParams.Refresh(
                key = null,
                loadSize = 10,
                placeholdersEnabled = false
            )
        )

        val error = assertIs<PagingSource.LoadResult.Error<Int, *>>(result)

        assertIs<ApiException.UnknownError>(error.throwable)
        assertEquals("Network error", result.throwable.cause?.message)
    }

}
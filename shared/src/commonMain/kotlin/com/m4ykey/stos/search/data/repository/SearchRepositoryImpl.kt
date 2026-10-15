package com.m4ykey.stos.search.data.repository

import androidx.paging.Pager
import androidx.paging.PagingData
import com.m4ykey.stos.core.paging.pagingConfig
import com.m4ykey.stos.question.domain.model.QuestionItem
import com.m4ykey.stos.search.domain.model.TagsItem
import com.m4ykey.stos.search.domain.repository.SearchRepository
import com.m4ykey.stos.search.data.remote.paging.SearchPagingSource
import com.m4ykey.stos.search.data.remote.paging.SearchPagingTags
import com.m4ykey.stos.search.data.remote.service.RemoteSearchService
import kotlinx.coroutines.flow.Flow

class SearchRepositoryImpl(
    private val service : RemoteSearchService
) : SearchRepository {

    override fun searchQuestions(inTitle: String, sort : String): Flow<PagingData<QuestionItem>> {
        return Pager(
            config = pagingConfig,
            pagingSourceFactory = {
                SearchPagingSource(service, inTitle, sort)
            }
        ).flow
    }

    override fun getSearchTags(): Flow<PagingData<TagsItem>> {
        return Pager(
            pagingSourceFactory = {
                SearchPagingTags(service)
            },
            config = pagingConfig
        ).flow
    }
}
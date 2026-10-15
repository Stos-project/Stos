package com.m4ykey.stos.search.data.mapper

import com.m4ykey.stos.search.domain.model.TagsItem
import com.m4ykey.stos.search.data.remote.model.TagsItemDto

fun TagsItemDto.toTagsItem() : TagsItem {
    return TagsItem(
        name = name.orEmpty()
    )
}
package com.m4ykey.stos.search.data.mapper

import com.m4ykey.stos.search.data.remote.model.TagsItemDto
import com.m4ykey.stos.search.domain.model.TagsItem

fun TagsItemDto.toTagsItem() : TagsItem {
    return TagsItem(
        name = name.orEmpty()
    )
}
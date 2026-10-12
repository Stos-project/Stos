package com.m4ykey.stos.search.mapper

import com.m4ykey.stos.search.domain.model.TagsItem
import com.m4ykey.stos.search.network.model.TagsItemDto

fun TagsItemDto.toTagsItem() : TagsItem {
    return TagsItem(
        name = name.orEmpty()
    )
}
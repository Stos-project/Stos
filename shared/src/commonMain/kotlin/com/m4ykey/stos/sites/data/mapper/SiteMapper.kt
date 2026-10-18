package com.m4ykey.stos.sites.data.mapper

import com.m4ykey.stos.sites.data.remote.model.SitesItemDto
import com.m4ykey.stos.sites.domain.model.SitesItem

fun SitesItemDto.toSites() : SitesItem {
    return SitesItem(
        apiSiteParameter = apiSiteParameter.orEmpty(),
        audience = audience.orEmpty(),
        faviconUrl = faviconUrl.orEmpty(),
        iconUrl = iconUrl.orEmpty(),
        logoUrl = logoUrl.orEmpty(),
        name = name.orEmpty(),
        siteUrl = siteUrl.orEmpty()
    )
}
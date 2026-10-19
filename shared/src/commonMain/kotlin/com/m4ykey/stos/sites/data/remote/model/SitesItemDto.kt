package com.m4ykey.stos.sites.data.remote.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SitesItemDto(
    @SerialName("favicon_url") val faviconUrl : String? = null,
    @SerialName("icon_url") val iconUrl : String? = null,
    val audience : String? = null,
    @SerialName("site_url") val siteUrl : String? = null,
    @SerialName("api_site_parameter") val apiSiteParameter : String? = null,
    @SerialName("logo_url") val logoUrl : String? = null,
    val name : String? = null
)

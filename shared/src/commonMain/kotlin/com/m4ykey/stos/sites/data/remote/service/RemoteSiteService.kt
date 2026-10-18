package com.m4ykey.stos.sites.data.remote.service

import com.m4ykey.stos.core.Filters.SITES_FILTER
import com.m4ykey.stos.core.model.Items
import com.m4ykey.stos.sites.data.remote.model.SitesItemDto

interface RemoteSiteService {

    suspend fun getSites(
        filter : String = SITES_FILTER,
        page : Int,
        pageSize : Int
    ) : Items<SitesItemDto>

}
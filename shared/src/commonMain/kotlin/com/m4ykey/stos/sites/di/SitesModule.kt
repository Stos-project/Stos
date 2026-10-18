package com.m4ykey.stos.sites.di

import com.m4ykey.stos.sites.data.helpers.SiteManager
import com.m4ykey.stos.sites.data.remote.service.RemoteSiteService
import com.m4ykey.stos.sites.data.remote.service.SiteService
import com.m4ykey.stos.sites.data.repository.SiteRepositoryImpl
import com.m4ykey.stos.sites.domain.repository.SiteRepository
import com.m4ykey.stos.sites.presentation.SitesViewModel
import org.koin.core.module.dsl.singleOf
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.bind
import org.koin.dsl.module

val sitesModule = module {
    singleOf(::SiteService) bind RemoteSiteService::class
    singleOf(::SiteRepositoryImpl) bind SiteRepository::class

    singleOf(::SiteManager)

    viewModelOf(::SitesViewModel)
}
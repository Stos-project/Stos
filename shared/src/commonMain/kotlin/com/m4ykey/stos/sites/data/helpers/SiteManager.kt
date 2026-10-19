package com.m4ykey.stos.sites.data.helpers

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class SiteManager {

    private val _selectedSite = MutableStateFlow("stackoverflow")
    val selectedSite = _selectedSite.asStateFlow()

    fun updateSite(site : String) {
        _selectedSite.value = site
    }

}
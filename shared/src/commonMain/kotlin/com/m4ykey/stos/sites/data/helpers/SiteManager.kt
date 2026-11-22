package com.m4ykey.stos.sites.data.helpers

import com.m4ykey.stos.sites.domain.model.SelectedSite
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class SiteManager {

    private val _selectedSite = MutableStateFlow(
        SelectedSite(
            apiSiteParameter = "stackoverflow",
            name = "Stack Overflow"
        )
    )
    val selectedSite : StateFlow<SelectedSite> = _selectedSite.asStateFlow()

    fun updateSite(site : SelectedSite) {
        _selectedSite.value = site
    }

}
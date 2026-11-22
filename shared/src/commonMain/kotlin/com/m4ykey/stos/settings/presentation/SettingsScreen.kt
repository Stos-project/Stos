package com.m4ykey.stos.settings.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.m4ykey.stos.core.network.openBrowser
import com.m4ykey.stos.core.ui.ActionButton
import com.m4ykey.stos.core.ui.AppScaffold
import com.m4ykey.stos.settings.presentation.components.SettingsItem
import org.jetbrains.compose.resources.stringResource
import stos.shared.generated.resources.Res
import stos.shared.generated.resources.back
import stos.shared.generated.resources.github
import stos.shared.generated.resources.ic_arrow_back
import stos.shared.generated.resources.ic_site
import stos.shared.generated.resources.settings
import stos.shared.generated.resources.settings_data_source
import stos.shared.generated.resources.settings_data_source_desc
import stos.shared.generated.resources.settings_disclaimer
import stos.shared.generated.resources.settings_github
import stos.shared.generated.resources.settings_github_subtitle

@Composable
fun SettingsScreen(
    onBack : () -> Unit,
    modifier : Modifier = Modifier
) {
    AppScaffold(
        modifier = modifier,
        title = stringResource(Res.string.settings),
        navigation = {
            ActionButton(
                icon = Res.drawable.ic_arrow_back,
                text = Res.string.back,
                onClick = onBack
            )
        },
        content = {
            SettingsContent()
        }
    )
}

@Composable
fun SettingsContent(modifier : Modifier = Modifier) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 10.dp, vertical = 15.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        SettingsItem(
            icon = Res.drawable.github,
            title = stringResource(Res.string.settings_github),
            onClick = { openBrowser("https://github.com/Stos-project/Stos") },
            subtitle = stringResource(Res.string.settings_github_subtitle)
        )
        SettingsItem(
            icon = Res.drawable.ic_site,
            title = stringResource(Res.string.settings_data_source),
            subtitle = stringResource(Res.string.settings_data_source_desc),
            onClick = { openBrowser("https://stackoverflow.com/help/licensing") }
        )
        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 8.dp),
            text = stringResource(Res.string.settings_disclaimer),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

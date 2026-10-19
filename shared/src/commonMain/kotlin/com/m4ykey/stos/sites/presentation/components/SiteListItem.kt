package com.m4ykey.stos.sites.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.m4ykey.stos.sites.domain.model.SitesItem
import com.m4ykey.text_markdown.TextMarkdown
import org.jetbrains.compose.resources.painterResource
import stos.shared.generated.resources.Res
import stos.shared.generated.resources.ic_check

@Composable
fun SiteListItem(
    modifier : Modifier = Modifier,
    item : SitesItem,
    isSelected : Boolean,
    onClick : () -> Unit
) {
    val audienceUppercase = item.audience.replaceFirstChar { it.uppercase() }

    Row(
        modifier = modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = item.iconUrl,
            contentDescription = item.name,
            contentScale = ContentScale.Crop,
            modifier = Modifier.size(48.dp)
        )
        Spacer(modifier = Modifier.width(10.dp))
        Column(modifier = Modifier.weight(1f)) {
            TextMarkdown(
                text = item.name,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                alignment = Alignment.TopStart
            )
            TextMarkdown(
                fontSize = 12.sp,
                alignment = Alignment.TopStart,
                fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                text = audienceUppercase
            )
        }

        if (isSelected) {
            Icon(
                contentDescription = "Selected",
                tint = MaterialTheme.colorScheme.primary,
                painter = painterResource(Res.drawable.ic_check),
                modifier = Modifier.size(24.dp)
            )
        }
    }
}
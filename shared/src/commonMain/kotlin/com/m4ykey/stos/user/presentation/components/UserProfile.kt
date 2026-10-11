package com.m4ykey.stos.user.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.m4ykey.stos.core.formatReputation
import com.m4ykey.stos.question.domain.model.BadgeCounts
import com.m4ykey.text_markdown.TextMarkdown

@Composable
fun UserProfile(
    profileImage : String,
    displayName : String,
    reputation : Int,
    badgeCounts : BadgeCounts
) {
    Box(
        modifier = Modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            UserCard(
                profileImage = profileImage,
                size = 200.dp
            )
            TextMarkdown(
                text = displayName
            )
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text(
                    text = formatReputation(reputation)
                )
                Spacer(modifier = Modifier.width(10.dp))
                BadgeCountsRow(badgeCounts = badgeCounts)
            }
        }
    }
}
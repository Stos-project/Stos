package com.m4ykey.stos.user.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.m4ykey.stos.core.formatCreationDate
import com.m4ykey.stos.core.formatReputation
import com.m4ykey.stos.user.domain.model.AnswerItem
import com.m4ykey.text_markdown.TextMarkdown

@Composable
fun AnswerListItem(
    modifier : Modifier = Modifier,
    item : AnswerItem,
    onQuestionClick : (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onQuestionClick(item.questionId) }
            .padding(bottom = 10.dp)
    ) {
        Row(
            modifier = modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            UserCard(profileImage = item.owner.profileImage)
            Spacer(modifier = modifier.width(10.dp))
            Column(modifier = Modifier.fillMaxWidth()) {
                TextMarkdown(
                    text = item.owner.displayName,
                    fontSize = 14.sp,
                    alignment = Alignment.TopStart
                )
                Text(
                    text = formatReputation(item.owner.reputation),
                    fontSize = 12.sp
                )
            }
        }
        TextMarkdown(
            alignment = Alignment.TopStart,
            text = item.title
        )
        Text(
            text = formatCreationDate(item.creationDate.toLong()),
            fontSize = 13.sp
        )
    }
}

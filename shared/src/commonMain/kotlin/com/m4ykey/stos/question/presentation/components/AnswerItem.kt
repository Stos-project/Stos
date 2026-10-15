package com.m4ykey.stos.question.presentation.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.m4ykey.stos.question.domain.model.QuestionAnswer
import com.m4ykey.stos.question.presentation.DisplayUser
import com.m4ykey.text_markdown.TextMarkdown
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import stos.shared.generated.resources.Res
import stos.shared.generated.resources.ic_check
import stos.shared.generated.resources.votes

@Composable
fun AnswerItem(
    answer : QuestionAnswer,
    onUserClick : (Int) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            if (answer.isAccepted) {
                Card(
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF25C706))
                ) {
                    Icon(
                        contentDescription = null,
                        painter = painterResource(Res.drawable.ic_check),
                        tint = Color.White,
                        modifier = Modifier.padding(5.dp)
                    )
                }
            }
            DisplayUser(
                profileImage = answer.owner.profileImage,
                displayName = answer.owner.displayName,
                reputation = answer.owner.reputation,
                badgeCounts = answer.owner.badgeCounts,
                onUserClick = onUserClick,
                userId = answer.owner.userId
            )
        }

        val score = answer.upVoteCount - answer.downVoteCount

        Text(
            text = "$score ${stringResource(Res.string.votes)}",
            fontSize = 14.sp
        )
        TextMarkdown(
            alignment = Alignment.TopStart,
            text = answer.bodyMarkdown
        )
    }
}
package com.m4ykey.stos.question.presentation.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.m4ykey.stos.core.formats.formatCreationDate
import com.m4ykey.stos.question.domain.model.BadgeCounts
import com.m4ykey.stos.question.presentation.DisplayUser
import com.m4ykey.text_markdown.TextMarkdown


@Composable
fun CommentItem (
    onOwnerClick : (Int) -> Unit,
    userId : Int,
    userProfile : String,
    displayName : String,
    reputation : Int,
    badgeCounts: BadgeCounts,
    text : String,
    date : Long
){
    Column (
        modifier = Modifier
            .padding(10.dp).fillMaxWidth()
    ) {
        DisplayUser(
            profileImage = userProfile,
            displayName = displayName,
            reputation = reputation,
            badgeCounts = badgeCounts,
            userId = userId,
            onUserClick = onOwnerClick
        )
        TextMarkdown(
            alignment = Alignment.TopStart,
            text = text
        )
        Text(
            fontSize = 12.sp,
            text = formatCreationDate(date)
        )
    }
}
@Preview
@Composable
fun CommentItemPrev (){
    val badgeCounts = BadgeCounts(
        gold = 123,
        silver = 123,
        bronze = 123
    )
    CommentItem (
        onOwnerClick = {},
        userId = 1,
        userProfile = "",
        displayName = "123",
        reputation = 1,
        badgeCounts = badgeCounts,
        text = "text222",
        date = 1304050195032903
    )
}
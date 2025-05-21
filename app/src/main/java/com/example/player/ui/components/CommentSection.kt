package com.example.player.ui.components


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp

@Composable
fun CommentSection(
    comments: List<Comments>,
    onCommentSubmit: (String) -> Unit
) {
    var commentText by remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(8.dp)) {
        Text(
            text = "Comments",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(start = 12.dp, bottom = 4.dp)
        )

        OutlinedTextField(
            value = commentText,
            onValueChange = { commentText = it },
            placeholder = { Text("Add a comment...") },
            modifier = Modifier.fillMaxWidth(),
            textStyle = TextStyle(color = Color.Black)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(
            onClick = {
                if (commentText.isNotBlank()) {
                    onCommentSubmit(commentText.trim())
                    commentText = ""
                }
            },
            modifier = Modifier.align(Alignment.End)
        ) {
            Text("Post")
        }

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn {
            items(comments.sortedByDescending { it.timestamp }) { comment ->
                Column(modifier = Modifier.padding(vertical = 4.dp)) {
                    Text(text = comment.username, style = MaterialTheme.typography.titleSmall)
                    Text(text = comment.content)
                }
            }
        }
    }
}

package com.example.player.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material.icons.filled.ThumbDown
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun LikeDislikeRow(modifier: Modifier, onNotesClick: () -> Unit) {
    var likes = remember{ mutableIntStateOf(0) }
    var dislikes = remember { mutableIntStateOf(0) }

    var liked = remember { mutableStateOf(false) }
    var disliked = remember { mutableStateOf(false) }

    Row (
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceEvenly
    ){
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = {
                    if (!liked.value){
                        likes.intValue++
                        if(disliked.value){
                            dislikes.intValue--
                            disliked.value = false
                        }
                    }
                    else likes.intValue--
                    liked.value = !liked.value

                }
            ) {
                Icon(Icons.Filled.ThumbUp, contentDescription = "Like")
            }
            Text(text = likes.intValue.toString())
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = {
                if (!disliked.value) {
                    dislikes.intValue++
                    if (liked.value) {
                        likes.intValue--
                        liked.value = false
                    }
                } else dislikes.intValue--
                disliked.value = !disliked.value
            }) {
                Icon(Icons.Filled.ThumbDown, contentDescription = "Dislike")
            }
            Text(text = dislikes.intValue.toString())
        }

        Button(onClick = onNotesClick) {
            Text("Notes")
        }
    }
}
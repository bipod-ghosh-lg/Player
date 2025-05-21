package com.example.player.ui.components
//
//import androidx.compose.foundation.layout.*
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.items
//import androidx.compose.material.*
//import androidx.compose.runtime.*
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.unit.dp
//
//@Composable
//fun NotesSection(
//    notes: List<String>,
//    onAddNote: (String) -> Unit
//) {
//    var noteText by remember { mutableStateOf("") }
//
//    Column(modifier = Modifier.fillMaxWidth().padding(8.dp)) {
//        Text(text = "Your Notes", style = MaterialTheme.typography.h6)
//
//        OutlinedTextField(
//            value = noteText,
//            onValueChange = { noteText = it },
//            label = { Text("Write a note...") },
//            modifier = Modifier.fillMaxWidth()
//        )
//
//        Spacer(modifier = Modifier.height(8.dp))
//
//        Button(
//            onClick = {
//                if (noteText.isNotBlank()) {
//                    onAddNote(noteText)
//                    noteText = ""
//                }
//            },
//            modifier = Modifier.align(Alignment.End)
//        ) {
//            Text("Save Note")
//        }
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        LazyColumn {
//            items(notes) { note ->
//                Text(text = "• $note", modifier = Modifier.padding(vertical = 4.dp))
//            }
//        }
//    }
//}


import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp

@Composable
fun NotesSection(
    notes: List<String>,
    onAddNote: (String) -> Unit
) {
    var noteText by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Text(
            text = "Your Notes",
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        OutlinedTextField(
            value = noteText,
            onValueChange = { noteText = it },
            label = { Text("Write a note...") },
            modifier = Modifier.fillMaxWidth(),
            textStyle = TextStyle(color = Color.Black)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            Button(
                onClick = {
                    if (noteText.isNotBlank()) {
                        onAddNote(noteText.trim())
                        noteText = ""
                    }
                }
            ) {
                Text("Save Note")
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        if (notes.isEmpty()) {
            Text(
                text = "Add your first note",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxWidth()
            ) {
                items(notes) { note ->
                    Text(
                        text = "• $note",
                        style = MaterialTheme.typography.bodyMedium,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )
                }
            }
        }
    }
}

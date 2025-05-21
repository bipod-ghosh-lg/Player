package com.example.player

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.ui.PlayerView
import com.example.player.ui.components.CommentSection
import com.example.player.ui.components.LikeDislikeRow
import com.example.player.ui.components.NotesSection
import com.example.player.viewmodel.VideoViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File


@Composable
fun DecryptAndPlayScreen(fileUri: Uri?, exoPlayer: Player) {


    val context = LocalContext.current
    val viewModel: VideoViewModel = viewModel()

    var decryptedFile by remember { mutableStateOf<File?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isReady by remember { mutableStateOf(false) }
    var resolvedName by remember { mutableStateOf<String?>(null) }

    val selectedFile by viewModel.selectedVideo.collectAsState()
    val comments by viewModel.comments.collectAsState()
    val notes by viewModel.notes.collectAsState()
    var showNotes by remember { mutableStateOf(false) }
    LaunchedEffect(fileUri) {
        if (fileUri == null) {
            errorMessage = "No file was selected"
            return@LaunchedEffect

        }

        try {
            val fileName = FileUtils.getFileNameFromUri(context, fileUri)
            if (fileName == null) {
                errorMessage = "Could not resolve file name"
                return@LaunchedEffect
            }

            resolvedName = fileName
            val outputFile = FileUtils.getDecryptedOutputFile(context, fileName)

            withContext(Dispatchers.IO) {
                VideoDecryptor.decryptToFile(context, fileUri, outputFile) { success ->
                    if (success && outputFile.exists()) {
                        decryptedFile = outputFile
                        isReady = true
                    } else {
                        errorMessage = "Decryption failed"
                    }
                }
            }

        } catch (e: Exception) {
            errorMessage = "Unexpected error: ${e.localizedMessage}"
            e.printStackTrace()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize().padding(2.dp)
    ) {
        when {
            errorMessage != null -> {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Error: $errorMessage",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center
                    )
                }
            }
            isReady && decryptedFile != null -> {
                // Safe to render AndroidView only now
                val fileId = selectedFile.hashCode().toString()
                AndroidView(
                    factory = {
                        PlayerView(it).apply {
                            player = exoPlayer
                            useController = true
                        }
//                        playerView!!
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(16f/ 9f)
                )


                LaunchedEffect(decryptedFile) {
                    decryptedFile?.let {
                        exoPlayer.setMediaItem(MediaItem.fromUri(it.toUri()))
                        exoPlayer.prepare()
                        exoPlayer.play()
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))

                resolvedName?.let {
                    val nameOfFile = it.substringBeforeLast(".")
                    Text(
                        text = nameOfFile,
                        style = MaterialTheme.typography.headlineMedium,
                        modifier = Modifier.padding(12.dp)
                    )
                }

//                Text(text = video.description, modifier = Modifier.padding(horizontal = 8.dp), style = MaterialTheme.typography.titleSmall)

                // Like/Dislike + Notes Toggle
                LikeDislikeRow(
                    modifier = Modifier.padding(horizontal = 12.dp,vertical = 8.dp),
                    onNotesClick = { showNotes = !showNotes }
                )

                // Notes Section
                if (showNotes) {
                    NotesSection(
                        notes = notes[fileId] ?: emptyList(),
                        onAddNote = { note -> viewModel.addNote(fileId, note) }
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Comment Section

                CommentSection(
                    comments = comments.filter { it.videoId == fileId },
                    onCommentSubmit = { content ->
                        viewModel.addComment(fileId, "User", content)
                    }
                )
            }
            else -> {
//                Text("")
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Decrypting video...",
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}
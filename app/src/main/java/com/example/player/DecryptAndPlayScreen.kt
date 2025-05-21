package com.example.player

import android.net.Uri
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.ui.PlayerView
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File


@Composable
fun DecryptAndPlayScreen(fileUri: Uri?, exoPlayer: Player) {


    val context = LocalContext.current
    var decryptedFile by remember { mutableStateOf<File?>(null) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isReady by remember { mutableStateOf(false) }

    // Decrypt only if URI is valid
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
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        when {
            errorMessage != null -> {
                Text("Error: $errorMessage", color = MaterialTheme.colorScheme.error)
            }
            isReady && decryptedFile != null -> {
                // Safe to render AndroidView only now
                AndroidView(
                    factory = {
                       PlayerView(context).apply {
                            player = exoPlayer
                        }
//                        playerView!!
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(250.dp)
                )

                LaunchedEffect(decryptedFile) {
                    decryptedFile?.let {
                        exoPlayer.setMediaItem(MediaItem.fromUri(it.toUri()))
                        exoPlayer.prepare()
                        exoPlayer.play()
                    }
                }
            }
            else -> {
                Text("Decrypting video...")
            }
        }
    }
}
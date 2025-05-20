package com.example.player

import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.example.player.ui.theme.PlayerTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File

class MainActivity : ComponentActivity() {

    private lateinit var exoPlayer: ExoPlayer
    private var playerView: PlayerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        exoPlayer = ExoPlayer.Builder(this).build()

        setContent {
            PlayerTheme {
                val uri = intent?.data
                DecryptAndPlayScreen(uri)
            }
        }
    }

    override fun onDestroy() {
        exoPlayer.release()
        super.onDestroy()
    }

    @Composable
    fun DecryptAndPlayScreen(fileUri: Uri?) {
        val context = this
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
                        runOnUiThread {
                            if (success && outputFile.exists()) {
                                decryptedFile = outputFile
                                isReady = true
                            } else {
                                errorMessage = "Decryption failed"
                            }
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
                            playerView = PlayerView(context).apply {
                                player = exoPlayer
                            }
                            playerView!!
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
}

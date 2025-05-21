package com.example.player

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.media3.exoplayer.ExoPlayer
import com.example.player.ui.theme.PlayerTheme


class MainActivity : ComponentActivity() {

    private lateinit var exoPlayer: ExoPlayer
//    private var playerView: PlayerView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        exoPlayer = ExoPlayer.Builder(this).build()

        setContent {
            PlayerTheme {
                val uri = intent?.data
                DecryptAndPlayScreen(uri, exoPlayer = exoPlayer)
            }
        }
    }

    override fun onDestroy() {
        exoPlayer.release()
        super.onDestroy()
    }

}

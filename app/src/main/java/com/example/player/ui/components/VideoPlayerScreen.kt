//package com.example.player.ui.components
//
//import android.provider.MediaStore.Video
//import androidx.compose.foundation.layout.*
//import androidx.compose.material3.Button
//import androidx.compose.material3.MaterialTheme
//import androidx.compose.material3.Text
//import androidx.compose.runtime.*
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.viewinterop.AndroidView
//import androidx.media3.common.MediaItem
//import androidx.media3.exoplayer.ExoPlayer
//import androidx.media3.ui.PlayerView
//import androidx.core.net.toUri
//import androidx.lifecycle.viewmodel.compose.viewModel
//import com.example.player.viewmodel.VideoViewModel
//
//@Composable
//fun VideoPlayerScreen(video: Video, onBack: () -> Unit) {
//    val context = LocalContext.current
//    val viewModel: VideoViewModel = viewModel()
//    val exoPlayer = remember {
//        ExoPlayer.Builder(context).build().apply {
//            setMediaItem(MediaItem.fromUri(video.url.toUri()))
//            prepare()
//            playWhenReady = true
//        }
//    }
//
//    val comments by viewModel.comments.collectAsState()
//    val notes by viewModel.notes.collectAsState()
//    var showNotes by remember { mutableStateOf(false) }
//
//    DisposableEffect(Unit) {
//        onDispose { exoPlayer.release() }
//    }
//
//    Column(modifier = Modifier.fillMaxSize()) {
//        AndroidView(
//            factory = {
//                PlayerView(it).apply {
//                    player = exoPlayer
//                    useController = true
//                }
//            },
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(250.dp)
//        )
//        Spacer(modifier = Modifier.height(12.dp))
//        Text(text = video.title, style = MaterialTheme.typography.headlineMedium, modifier = Modifier.padding(8.dp))
//
//        LikeDislikeRow(
//            modifier = Modifier.padding(vertical = 8.dp),
//            onNotesClick = { showNotes = !showNotes }
//        )
//
//        if (showNotes) {
//            NotesSection(
//                notes = notes[video.id] ?: emptyList(),
//                onAddNote = { viewModel.addNote(video.id, it) }
//            )
//        }
//
//        Text(text = video.description, modifier = Modifier.padding(horizontal = 8.dp), style = MaterialTheme.typography.titleSmall)
//
//        Spacer(modifier = Modifier.height(12.dp))
//
//        CommentSection(
//            comments = comments.filter { it.videoId == video.id },
//            onCommentSubmit = { content ->
//                viewModel.addComment(video.id, "User", content)
//            }
//        )
//
//        Spacer(modifier = Modifier.height(8.dp))
//        Button(onClick = onBack, modifier = Modifier.padding(8.dp)) {
//            Text("<- Back")
//        }
//    }
//}

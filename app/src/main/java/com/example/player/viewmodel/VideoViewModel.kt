package com.example.player.viewmodel

import android.provider.MediaStore.Video
import androidx.lifecycle.ViewModel
import com.example.player.ui.components.Comments
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class VideoViewModel : ViewModel() {
    //Selected Video
    private val _selectedVideo = MutableStateFlow<Video?>(null)         // The variable which store the mutable section that can interact with the Model and pass the necessary changes to the view
    val selectedVideo: StateFlow<Video?> = _selectedVideo                // This variable is immutable which restrict the user from causing any changes on the UI by themselves

    //Comments
    private val _comments = MutableStateFlow<List<Comments>>(emptyList())
    val comments: MutableStateFlow<List<Comments>> = _comments

    //Notes
    private val _notes = MutableStateFlow<Map<String, List<String>>>(emptyMap())
    val notes: StateFlow<Map<String, List<String>>> = _notes


    fun addComment(videoId: String, username: String, content: String) {
        val newComment = Comments(videoId, username, content)
        _comments.value = _comments.value + newComment
    }

    fun addNote(videoId: String, note: String) {
        val currentNotes = _notes.value[videoId] ?: emptyList()
        _notes.value = _notes.value + (videoId to (currentNotes + note))
    }


}

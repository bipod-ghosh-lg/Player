package com.example.player.screenshotprevention.viewmodel

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class  MainViewModel : ViewModel() {
    private val _secureMessage = MutableStateFlow("This screen is protected from screenshots.")
    val secureMessage: StateFlow<String> = _secureMessage
}

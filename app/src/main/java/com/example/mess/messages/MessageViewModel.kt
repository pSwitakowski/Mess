package com.example.mess.messages

import androidx.lifecycle.ViewModel
import com.example.mess.repository.FirebaseRepository

class MessagesViewModel : ViewModel() {
    private val repository = FirebaseRepository()

    val messages = repository.getAllMessages()
}
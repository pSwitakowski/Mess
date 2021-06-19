package com.example.mess.profile

import androidx.lifecycle.ViewModel
import com.example.mess.repository.FirebaseRepository

class ProfileViewModel : ViewModel() {
    private val repository = FirebaseRepository()

    val user = repository.getUserData()
}
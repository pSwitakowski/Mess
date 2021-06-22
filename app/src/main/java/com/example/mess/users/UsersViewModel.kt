package com.example.mess.users

import androidx.lifecycle.ViewModel
import com.example.mess.repository.FirebaseRepository

class UsersViewModel : ViewModel() {
    private val repository = FirebaseRepository()

    val users = repository.getUsers()
}
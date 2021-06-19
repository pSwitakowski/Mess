package com.example.mess.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mess.R
import com.google.firebase.auth.FirebaseAuth

class UserAuthActivity : AppCompatActivity() {

    private val fbAuth: FirebaseAuth = FirebaseAuth.getInstance()

    override fun onStart() {
        super.onStart()

    }

    private fun isCurrentUser(){
        fbAuth.currentUser?.let {auth ->
            val intent = Intent(applicationContext, MainActivity::class.java).apply {
                flags = (Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            }
            startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_userauth)
        isCurrentUser()
    }
}
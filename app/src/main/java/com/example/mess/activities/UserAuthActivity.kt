package com.example.mess.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.mess.R
import com.example.mess.repository.FirebaseRepository
import com.google.firebase.auth.FirebaseAuth

class UserAuthActivity : AppCompatActivity() {

    private val fbAuth: FirebaseAuth = FirebaseAuth.getInstance()
    private val firebaseRepository = FirebaseRepository()
    private val authStateListener = FirebaseAuth.AuthStateListener { fbAuth->
        val firebaseUser = fbAuth.currentUser
        if (firebaseUser != null) {
            firebaseRepository.setUserOnlineStatus(fbAuth.currentUser!!.uid, true)
            val intent = Intent(applicationContext, MainActivity::class.java).apply {
                flags = (Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            }
            startActivity(intent)
            finish()
        }
    }

    override fun onStart() {
        super.onStart()
        fbAuth.addAuthStateListener(this.authStateListener)
    }
    override fun onStop() {
        super.onStop()
        fbAuth.removeAuthStateListener(this.authStateListener)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_userauth)
    }

}
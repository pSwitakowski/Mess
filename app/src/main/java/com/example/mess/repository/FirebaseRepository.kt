package com.example.mess.repository

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mess.data.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class FirebaseRepository {
    private val REPO_DEBUG = "REPO_DEBUG"
    private val auth = FirebaseAuth.getInstance()
    private val cloud = FirebaseFirestore.getInstance()

    fun getUserData(): LiveData<User> {
        val cloudResult = MutableLiveData<User>()
        val uid = auth.currentUser?.uid

        cloud.collection("users")
            .document(uid!!)
            .get()
            .addOnSuccessListener {
                val user = it.toObject(User::class.java)
                cloudResult.postValue(user)
            }
            .addOnFailureListener {
                Log.d(REPO_DEBUG, it.message.toString())
            }
        return cloudResult
    }

    fun createUser(username:CharSequence, email: CharSequence, uid: CharSequence){

        val user = hashMapOf(
            "name" to username,
            "email" to email,
            "uid" to uid
        )

        cloud.collection("users").document(uid.toString())
            .set(user)
            .addOnSuccessListener {
                Log.d(REPO_DEBUG, "User successfully created")
            }
            .addOnFailureListener { e ->
                Log.w(REPO_DEBUG, "Error creating user", e)
            }

    }

    fun setUserOnlineStatus(uid: CharSequence, online: Boolean){

        cloud.collection("users").document(uid.toString())
            .update("online", online)
            .addOnSuccessListener {
                Log.d(REPO_DEBUG, "User " + auth.currentUser?.uid + " online status changed to: " + online.toString())
            }
            .addOnFailureListener { e ->
                Log.w(REPO_DEBUG, "Error setting user " + auth.currentUser?.uid + " online status")
            }
    }

    fun getUsers() : LiveData<List<User>>{
        val cloudResult = MutableLiveData<List<User>>()

        cloud.collection("users")
            .whereNotEqualTo("uid", auth.currentUser?.uid)
            .get()
            .addOnSuccessListener {
                val user = it.toObjects(User::class.java)
                cloudResult.postValue(user)
            }
            .addOnFailureListener {
                Log.d(REPO_DEBUG, it.message.toString())
            }
        return cloudResult
    }
}

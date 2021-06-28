package com.example.mess.repository

import android.service.autofill.Validators.not
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.mess.data.Message
import com.example.mess.data.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.google.firebase.Timestamp
import java.util.*

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
                val users = it.toObjects(User::class.java)

                // sort by online at first
                users.sortWith { user1, user2 ->
                    user1.online!!.compareTo(user2.online!!)
                }
                users.reverse()

                cloudResult.postValue(users)
            }
            .addOnFailureListener {
                Log.d(REPO_DEBUG, it.message.toString())
            }

        return cloudResult
    }

    fun getAllMessages() : LiveData<List<Message>>{
        val cloudResult = MutableLiveData<List<Message>>()

        cloud.collection("messages")
            //.whereEqualTo("author_uid", auth.currentUser!!.uid)
            //.whereEqualTo("recipient_uid", recipient_uid)
            //.whereEqualTo("recipient_uid", recipient_uid)
            .orderBy("time", Query.Direction.ASCENDING)
            //.limit(10)
            .get()
            .addOnSuccessListener {
                val messages = it.toObjects(Message::class.java)
                cloudResult.postValue(messages)
            }
            .addOnFailureListener {
                Log.d(REPO_DEBUG, it.message.toString())
            }
        return cloudResult
    }

    fun createMessage(recipient_uid: CharSequence, message_input: CharSequence){

        val message = hashMapOf(
            "recipient_uid" to recipient_uid,
            "message" to message_input.toString(),
            "author_uid" to auth.currentUser!!.uid,
            "time" to Timestamp.now()
        )

        cloud.collection("messages")
            .document()
            .set(message)
            .addOnSuccessListener {
                Log.d(REPO_DEBUG, "Message successfully created")
            }
            .addOnFailureListener { e ->
                Log.w(REPO_DEBUG, "Error creating message", e)
            }
    }


}


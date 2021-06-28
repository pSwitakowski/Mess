package com.example.mess.data
import android.os.IBinder
import com.google.firebase.Timestamp
import com.google.firebase.firestore.ServerTimestamp


data class Message(val message: String? = null,
                   val author_uid: String? = null,
                   val recipient_uid: String? = null,
                   @ServerTimestamp
                   val time: Timestamp? = Timestamp.now()
)
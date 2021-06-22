package com.example.mess.data

import android.os.IBinder

data class Message(val message: String? = null,
                val time: String? = null, //change type!
                val autor_uid: String? = null,
                val recipient_uid: String? = null
)
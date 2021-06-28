package com.example.mess.messages

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ScrollView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mess.R
import com.example.mess.activities.MainActivity
import com.example.mess.data.Message
import com.example.mess.repository.FirebaseRepository
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.fragment_messages.*


class MessagesFragment : Fragment(), Onclickable{

    private val messageVm by viewModels<MessagesViewModel>()
    private val adapter = MessageAdapter(this)
    private val cloud = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private var messagesRef = cloud.collection("messages")
        .orderBy("time", Query.Direction.ASCENDING)
    private val repo = FirebaseRepository()
    private var listener: ListenerRegistration? = null
    private var recipient: String? = null
    private var recipientUid: String? = null
    private var recipientName: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        Log.d("RECIPIENT_UID", arguments?.getString("recipient_uid")!!)
        Log.d("AUTHOR_UID", auth.currentUser!!.uid)
        this.recipientUid = arguments?.getString("recipient_uid")
        this.recipientName = arguments?.getString("recipient_name")

        // real-time messages update
        listener = messagesRef.addSnapshotListener { querySnapshot, exc ->
            querySnapshot?.let {
                val messagesList = ArrayList<Message>()
                for (document in it) {
                    val message = document.toObject(Message::class.java)
                    if((message.author_uid == auth.currentUser!!.uid && message.recipient_uid == this.recipientUid)
                        || (message.author_uid == this.recipientUid && message.recipient_uid == auth.currentUser!!.uid)
                    ) messagesList.add(message)

                }
                Log.d("MESSAGES_UPDATED", messagesList.size.toString())
                adapter.setMessages(messagesList)
                message_scroll_view.post { message_scroll_view.fullScroll(ScrollView.FOCUS_DOWN) }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_messages, container, false)
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        messages_recycler_view.layoutManager = LinearLayoutManager(requireContext())
        messages_recycler_view.adapter = adapter

        message_recipient_uid.text = this.recipientName

        // send message button onclick
        message_send_btn.setOnClickListener {
            if (message_input_et.text.toString() != "")
                repo.createMessage(this.recipientUid.toString(), message_input_et.text.toString())
            message_input_et.setText("")

            message_scroll_view.post { message_scroll_view.fullScroll(ScrollView.FOCUS_DOWN) }
        }

        // scroll to messages bottom
        message_scroll_view.postDelayed({ message_scroll_view.fullScroll(ScrollView.FOCUS_DOWN) }, 250)


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
//        messageVm.messages.observe(viewLifecycleOwner, {messages ->
//            adapter.setMessages(messages)
//        })
    }

    override fun onStop() {
        super.onStop()
        listener!!.remove()
    }

    override fun onMessageLongClick(message: Message, position: Int) {
        Toast.makeText(requireContext(), message.toString(), Toast.LENGTH_SHORT)
            .show()
    }

    override fun onMessageClick(message: Message, position: Int) {

    }

}
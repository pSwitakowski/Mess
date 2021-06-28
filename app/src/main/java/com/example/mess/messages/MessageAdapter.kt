package com.example.mess.messages

import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.mess.R
import com.example.mess.data.Message
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_messages.*
import kotlinx.android.synthetic.main.message_item.view.*


class MessageAdapter(private val listener: Onclickable): RecyclerView.Adapter<MessageAdapter.MessageViewHolder>() {

    private val messagesList = ArrayList<Message>()
    private val auth = FirebaseAuth.getInstance()

    fun setMessages(list: List<Message>) {
        messagesList.clear()
        messagesList.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MessageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.message_item, parent, false)
        return MessageViewHolder(view)
    }

    override fun onBindViewHolder(holder: MessageViewHolder, position: Int) {
        bindData(holder)
    }

    fun bindData(holder: MessageViewHolder) {
        //val author = holder.itemView.findViewById<TextView>(R.id.message_item_name_tv)
        val message = holder.itemView.findViewById<TextView>(R.id.message_item_message_tv)
        //val time = holder.itemView.findViewById<TextView>(R.id.message_item_time_tv)



        if(messagesList[holder.bindingAdapterPosition].author_uid == auth.currentUser!!.uid) {
            holder.itemView.findViewById<CardView>(R.id.message_card_view).apply {
                layoutDirection = View.LAYOUT_DIRECTION_RTL
                setCardBackgroundColor(0xFFE6E695.toInt())
                x = 500f
            }

        }


        //holder.itemView.findViewById<CardView>(R.id.message_card_view).x=holder.itemView.findViewById<CardView>(R.id.message_card_view)
            //holder.itemView.findViewById<CardView>(R.id.message_card_view).requestLayout()

        //author.text = messagesList[holder.bindingAdapterPosition].author_uid
        message.text = messagesList[holder.bindingAdapterPosition].message
        //val date = messagesList[holder.bindingAdapterPosition].time?.toDate()
//        time.text = date!!.hours.toString()
//            .plus(":")
//            .plus(date.minutes.toString())
//            .plus(":")
//            .plus(date.seconds.toString())
    }

    override fun getItemCount(): Int {
        return messagesList.size
    }

    inner class MessageViewHolder(view: View) : RecyclerView.ViewHolder(view){
        init{
            view.setOnLongClickListener {
                listener.onMessageLongClick(messagesList[bindingAdapterPosition], bindingAdapterPosition)
                true
            }
            view.setOnClickListener {
                listener.onMessageClick(messagesList[bindingAdapterPosition], bindingAdapterPosition)
                true
            }
            }
        }
}

interface Onclickable{
    fun onMessageLongClick(message: Message, position: Int)
    fun onMessageClick(message: Message, position: Int)
}
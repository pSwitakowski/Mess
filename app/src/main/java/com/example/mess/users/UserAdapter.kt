package com.example.mess.users

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mess.R
import com.example.mess.data.User

class UserAdapter(private val listener: OnUserItemLongClick): RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    private val usersList = ArrayList<User>()

    fun setUsers(list: List<User>) {
        usersList.clear()
        usersList.addAll(list)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.user_item, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        bindData(holder)
    }

    private fun bindData(holder: UserViewHolder) {
        val name = holder.itemView.findViewById<TextView>(R.id.user_item_name_tv)
        val email = holder.itemView.findViewById<TextView>(R.id.user_item_email_tv)
        val online = holder.itemView.findViewById<TextView>(R.id.user_item_online_tv)

        name.text = usersList[holder.absoluteAdapterPosition].name
        email.text = usersList[holder.absoluteAdapterPosition].email
        online.text = "Online: " + usersList[holder.absoluteAdapterPosition].online.toString()
    }

    override fun getItemCount(): Int {
        return usersList.size
    }

    inner class UserViewHolder(view: View) : RecyclerView.ViewHolder(view){
        init{
            view.setOnLongClickListener {
                listener.onUserLongClick(usersList[absoluteAdapterPosition], absoluteAdapterPosition)
                true
            }
            }
        }
}

interface OnUserItemLongClick{
    fun onUserLongClick(user: User, position: Int)
}

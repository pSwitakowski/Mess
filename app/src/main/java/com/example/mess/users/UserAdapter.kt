package com.example.mess.users

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mess.R
import com.example.mess.data.User


class UserAdapter(private val listener: Onclickable): RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

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

    fun bindData(holder: UserViewHolder) {
        val name = holder.itemView.findViewById<TextView>(R.id.user_item_name_tv)
        val email = holder.itemView.findViewById<TextView>(R.id.user_item_email_tv)
        val online_img = holder.itemView.findViewById<ImageView>(R.id.user_item_online_iv)

        name.text = usersList[holder.bindingAdapterPosition].name
        email.text = usersList[holder.bindingAdapterPosition].email
        val online_status = usersList[holder.bindingAdapterPosition].online.toString()
        if(online_status=="true") {
            online_img.setBackgroundResource(R.drawable.circle_green)
        } else if(online_status=="false")
        {
            online_img.setBackgroundResource(R.drawable.circle_red)
        }
    }

    override fun getItemCount(): Int {
        return usersList.size
    }

    inner class UserViewHolder(view: View) : RecyclerView.ViewHolder(view){
        init{
            view.setOnLongClickListener {
                listener.onUserLongClick(usersList[bindingAdapterPosition], bindingAdapterPosition)
                true
            }
            view.setOnClickListener {
                listener.onUserClick(view, usersList[bindingAdapterPosition], bindingAdapterPosition)
                true
            }
            }
        }

//    var options: FirestoreRecyclerOptions<ProductModel> = Builder<ProductModel>()
//        .setQuery(query, ProductModel::class.java)
//        .build()
//
//    private class ProductViewHolder internal constructor(private val view: View) :
//        RecyclerView.ViewHolder(view) {
//        fun setProductName(productName: String?) {
//            val textView = view.findViewById<TextView>(R.id.text_view)
//            textView.text = productName
//        }
//    }
}

interface Onclickable{
    fun onUserLongClick(user: User, position: Int)
    fun onUserClick(view:View, user: User, position: Int)
}

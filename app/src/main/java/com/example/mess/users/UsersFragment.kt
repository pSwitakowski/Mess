package com.example.mess.users

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mess.R
import com.example.mess.data.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.fragment_users.*


class UsersFragment : Fragment(), Onclickable {
    private val userVm by viewModels<UsersViewModel>()
    private val adapter = UserAdapter(this)
    private val cloud = FirebaseFirestore.getInstance()
    private val usersRef = cloud.collection("users")
    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)



        usersRef.addSnapshotListener { querySnapshot, exc ->
            querySnapshot?.let {
                val usersList = ArrayList<User>()
                for (document in it) {
                    val user = document.toObject(User::class.java)
                    if (user.uid != auth.currentUser?.uid)
                        usersList.add(user)
                }
                // sort by online
                usersList.sortWith { user1, user2 ->
                    user1.online!!.compareTo(user2.online!!)
                }
                usersList.reverse()
                adapter.setUsers(usersList)
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_users, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        users_recycler_view.layoutManager = LinearLayoutManager(requireContext())
        users_recycler_view.adapter = adapter
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        userVm.users.observe(viewLifecycleOwner, {users ->
            adapter.setUsers(users)
        })

    }

    override fun onUserLongClick(user: User, position: Int) {
        Toast.makeText(requireContext(), user.toString(), Toast.LENGTH_SHORT)
            .show()
    }

    override fun onUserClick(view: View, user: User, position: Int) {
        val bundle = bundleOf("recipient_uid" to user.uid, "recipient_name" to user.name)
        Navigation.findNavController(view).navigate(R.id.messagesFragment, bundle)

    }


}
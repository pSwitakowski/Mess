package com.example.mess.users

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mess.R
import com.example.mess.data.User
import kotlinx.android.synthetic.main.fragment_users.*

class UsersFragment : Fragment(), OnUserItemLongClick {
    private val userVm by viewModels<UsersViewModel>()
    private val adapter = UserAdapter(this)

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
}
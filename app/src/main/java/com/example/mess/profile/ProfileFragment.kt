package com.example.mess.profile

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.mess.R
import com.example.mess.data.User
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_profile.*


class ProfileFragment : Fragment() {
    private val PROFILE_DEBUG = "PROFILE_DEBUG"
    private val fbAuth = FirebaseAuth.getInstance()
    private val profileViewmodel by viewModels<ProfileViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        setHasOptionsMenu(true)
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_profile, container, false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.logout_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.home_logout_item -> {
                fbAuth.signOut()
                requireActivity().finish()
            }
        }
        return super.onOptionsItemSelected(item)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        profileViewmodel.user.observe(viewLifecycleOwner, { user ->
            bindUserData(user)
        })
    }

    private fun bindUserData(user: User) {
        Log.d(PROFILE_DEBUG, user.toString())
        profile_email_tv.setText(user.email)
        profile_name_tv.setText(user.name)
        //details section
        profile_details_email_tv.setText(user.email)
        profile_details_name_tv.setText(user.name)
    }

}
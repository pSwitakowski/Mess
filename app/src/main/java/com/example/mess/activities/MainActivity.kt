package com.example.mess.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.mess.R
import com.example.mess.data.User
import com.example.mess.messages.MessagesFragment
import com.example.mess.users.Onclickable
import com.example.mess.users.UsersFragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth

class MainActivity : AppCompatActivity(), Onclickable {

    private val fragmentUsers: UsersFragment = UsersFragment()
    private val fragmentMessages: MessagesFragment = MessagesFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


        // Navbar setup
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val navController = findNavController(R.id.fragment_navhost_mainactivity)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.profileFragment,
                R.id.usersFragment,
                R.id.messagesFragment
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        bottomNavigationView.setupWithNavController(navController)

        // hide bottom nav bar when on messages view
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.messagesFragment -> {
                    hideBottomNavigationView(bottomNavigationView)
                }
                R.id.usersFragment -> showBottomNavigationView(bottomNavigationView)
                R.id.profileFragment -> showBottomNavigationView(bottomNavigationView)
            }
        }


    }

    private fun hideBottomNavigationView(view: BottomNavigationView) {
        view.clearAnimation()
        view.animate().translationY(view.height.toFloat()).duration = 300
    }

    fun showBottomNavigationView(view: BottomNavigationView) {
        view.clearAnimation()
        view.animate().translationY(0f).duration = 300
    }

    override fun onBackPressed() {
        super.onBackPressed()

//        val currentFragment: Fragment = supportFragmentManager.findFragmentById(R.id.messages_fragment_tv)!!
//        if (currentFragment !is MessagesFragment) {
//            findNavController()
//                .navigate(Layout.Directions.actionLoginFragmentToRegistrationFragment().actionId)
//        }
    }

    override fun onUserLongClick(user: User, position: Int) {
        Log.d("USER LONG CLICK", user.toString())
    }

    override fun onUserClick(view: View, user: User, position: Int) {

    }

}


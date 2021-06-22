package com.example.mess.activities

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.mess.R
import com.example.mess.repository.FirebaseRepository
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Navbar setup
        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.bottomNavigationView)
        val navController = findNavController(R.id.fragment_navhost_mainactivity)
        var appBarConfiguration = AppBarConfiguration(setOf(R.id.profileFragment,R.id.homeFragment))
        setupActionBarWithNavController(navController, appBarConfiguration)
        bottomNavigationView.setupWithNavController(navController)

    }

    // Overrride back press on main activity so it doesnt log out the user
    override fun onBackPressed() {
        super.onBackPressed()
    }

//
//
//        //Sign-in button
//        val signInButton = findViewById<SignInButton>(R.id.sign_in_button)
//        signInButton.setSize(SignInButton.SIZE_STANDARD)
//
//        // Google sign-in auth
//        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//            .build()
//        val mGoogleSignInClient = GoogleSignIn.getClient(this, gso)
//        val account = GoogleSignIn.getLastSignedInAccount(this)
//        updateUI(account)
//
//
//
//        signInButton.setOnClickListener {
//            signIn(mGoogleSignInClient)
//            //Toast.makeText(this, "sign in button click", Toast.LENGTH_SHORT).show()
//        }


//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        //super.onActivityResult(requestCode, resultCode, data)
//
//        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
//        if (requestCode == 1) {
//            // The Task returned from this call is always completed, no need to attach
//            // a listener.
//            val task: Task<GoogleSignInAccount> = GoogleSignIn.getSignedInAccountFromIntent(data)
//
//            handleSignInResult(task)
//        }
//    }
//    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
//        try {
//            val account = completedTask.getResult(ApiException::class.java)
//
//            // Signed in successfully, show authenticated UI.
//            showLoggedAccountInfo(account)
//        } catch (e: ApiException) {
//            // The ApiException status code indicates the detailed failure reason.
//            // Please refer to the GoogleSignInStatusCodes class reference for more information.
//            Log.w(TAG, "signInResult:failed code=" + e.statusCode)
//            //updateUI(account)
//        }
//    }
//
//    private fun showLoggedAccountInfo(account: GoogleSignInAccount?) {
//        Toast.makeText(this, ""+ account?.account.toString(), Toast.LENGTH_LONG).show()
//    }
}
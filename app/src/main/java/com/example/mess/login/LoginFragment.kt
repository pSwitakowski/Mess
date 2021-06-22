package com.example.mess.login

import android.app.Activity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.navigation.fragment.findNavController
import com.example.mess.BaseFragment
import com.example.mess.R
import com.example.mess.repository.FirebaseRepository
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_login.*


class LoginFragment : BaseFragment() {
    private val fbAuth = FirebaseAuth.getInstance()
    private val LOG_DEBUG = "LOGIN_DEBUG"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupLoginClick()
        setupRegistrationClick()
    }

    private fun setupRegistrationClick() {
        login_createaccount_btn.setOnClickListener{
            findNavController()
                .navigate(LoginFragmentDirections.actionLoginFragmentToRegistrationFragment().actionId)
        }
    }

    private fun setupLoginClick() {
        login_btn.setOnClickListener {
            val email = login_email_et.text?.trim().toString()
            val password = login_pwd_et.text?.trim().toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                fbAuth.signInWithEmailAndPassword(email, password)
                    .addOnSuccessListener { authRes ->
                        if (authRes.user != null) {
                            // set user status on db to online

                            // Auth observer
                            //firebaseRepository.setUserOnlineStatus(authRes.user!!.uid, true)

                            // start main activity
                            startApp()

                        }

                    }
                    .addOnFailureListener { exc ->

                        //hide keyboard when showing Snackbar
                        val imm: InputMethodManager =
                            activity!!.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.hideSoftInputFromWindow(view!!.windowToken, 0)

                        Snackbar.make(requireView(), "Bad credentials. Try again", Snackbar.LENGTH_SHORT)
                            .show()
                        Log.d(LOG_DEBUG, exc.message.toString())
                    }
            }
        }

    }
}
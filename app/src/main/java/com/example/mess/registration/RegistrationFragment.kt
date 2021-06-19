package com.example.mess.registration

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.mess.BaseFragment
import com.example.mess.R
import com.example.mess.repository.FirebaseRepository
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.fragment_registration.*

class RegistrationFragment : BaseFragment() {
    private val REG_DEBUG = "REG_DEBUG"
    private val fbAuth = FirebaseAuth.getInstance()

    private val firebaseRepository = FirebaseRepository()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_registration, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpRegistrationClick()

    }

    private fun setUpRegistrationClick() {
        register_btn.setOnClickListener {
            val name = register_name_et.text?.trim().toString()
            val email = register_email_et.text?.trim().toString()
            val pwd1 = register_pwd1.text?.trim().toString()
            val pwd2 = register_pwd2.text?.trim().toString()

            if((pwd1 == pwd2) && (name.isNotEmpty() && email.isNotEmpty() && pwd1.isNotEmpty() && pwd2.isNotEmpty())){
                fbAuth.createUserWithEmailAndPassword(email, pwd1)
                    .addOnSuccessListener { authRes ->

                        if (authRes.user != null) {
                            firebaseRepository.createUser(name, email, authRes.user!!.uid)
                            startApp()
                        }
                    }
                    .addOnFailureListener { exc ->
                        Snackbar.make(requireView(), "RegisterClick FAILURE", Snackbar.LENGTH_SHORT)
                            .show()
                        Log.d(REG_DEBUG, exc.message.toString())
                    }
            }
        }
    }
}
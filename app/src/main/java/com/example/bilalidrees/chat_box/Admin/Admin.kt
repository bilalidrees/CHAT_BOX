package com.example.bilalidrees.chat_box.Admin

import android.content.Intent
import android.support.v4.app.Fragment
import android.os.Bundle
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.bilalidrees.chat_box.R
import com.google.firebase.auth.FirebaseAuth

open class Admin  :Fragment(),View.OnClickListener {
    private lateinit var mAuth: FirebaseAuth
    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val view = inflater!!.inflate(R.layout.admin_login, container, false)
        mAuth = FirebaseAuth.getInstance()


        editTextEmail = view.findViewById(R.id.editTextEmail) as EditText
        editTextPassword = view.findViewById(R.id.editTextPassword) as EditText



        view.findViewById<Button>(R.id.buttonLogin).setOnClickListener(this)

       
        return view



    }

    private fun userLogin() {
        val email = editTextEmail.text.toString().trim { it <= ' ' }
        val password = editTextPassword.text.toString().trim { it <= ' ' }

        if (email.isEmpty()) {
            editTextEmail.error = "Email is required"
            editTextEmail.requestFocus()
            return
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            editTextEmail.error = "Please enter a valid email"
            editTextEmail.requestFocus()
            return
        }

        if (password.isEmpty()) {
            editTextPassword.error = "Password is required"
            editTextPassword.requestFocus()
            return
        }

        if (password.length < 6) {
            editTextPassword.error = "Minimum lenght of password should be 6"
            editTextPassword.requestFocus()
            return
        }



        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener { task ->
            if (task.isSuccessful) {

                if(mAuth.currentUser!!.email.equals("admin@gmail.com")){
                    val intent = Intent(context, Admin_seleciton_chat::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    startActivityForResult(intent, 1)
                    activity!!.finish()

                }
                else{
                    Toast.makeText(context,"admin id  is  required", Toast.LENGTH_SHORT).show()

                }

            } else {
                Toast.makeText(context, task.exception!!.message, Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == 0) {
            activity!!.finish()
        }
    }

    override fun onStart() {
        super.onStart()

        if (mAuth.currentUser != null) {

            val intent = Intent(context, Admin_seleciton_chat::class.java)
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivityForResult(intent, 1)
            activity!!.finish()
        }
    }





    override fun onClick(view: View?) {

        when (view!!.getId()) {

            R.id.buttonLogin -> userLogin()
        }
    }

}
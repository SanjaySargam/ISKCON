package com.example.iskcon

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class SignInActivity : AppCompatActivity() {
    private var email: EditText? = null
    private  var pass:EditText? = null
    private var mAuth: FirebaseAuth? = null
    private var progressDialog: Dialog? = null
    private var dialogText: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        email = findViewById(R.id.email)
        pass = findViewById(R.id.password)
        val loginBtn = findViewById<Button>(R.id.loginBtn)
        mAuth = FirebaseAuth.getInstance()

        progressDialog = Dialog(this@SignInActivity)
        progressDialog!!.setContentView(R.layout.dialog_layout)
        progressDialog!!.setCancelable(false)
        progressDialog!!.getWindow()
            ?.setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        dialogText = progressDialog!!.findViewById(R.id.dialog_text)
        dialogText!!.setText("Signing in...")


        loginBtn.setOnClickListener {
            if (validateData()) {
                login()
            }
        }
    }
    private fun validateData(): Boolean {
        if (email!!.text.toString().isEmpty()) {
            email!!.error = "Enter E-mail ID"
            return false
        }
        if (pass!!.text.toString().isEmpty()) {
            pass!!.error = "Enter Password"
            return false
        }
        return true
    }
    private fun login() {
        progressDialog!!.show()
        mAuth!!.signInWithEmailAndPassword(
            email!!.text.toString().trim { it <= ' ' },
            pass!!.text.toString().trim { it <= ' ' })
            .addOnCompleteListener(
                this
            ) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    progressDialog!!.dismiss()
                    Toast.makeText(this@SignInActivity, "Login Success", Toast.LENGTH_SHORT).show()
                    val intent=Intent(this@SignInActivity,MainActivity::class.java)
                    startActivity(intent)
                }
                else{
                    progressDialog!!.dismiss()
                    Toast.makeText(this@SignInActivity, "Invalid Credentials", Toast.LENGTH_SHORT).show()

                }

            }
    }
}
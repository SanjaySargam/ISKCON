package com.example.iskcon

import android.app.Dialog
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.iskcon.databinding.ActivityMainBinding
import com.example.iskcon.databinding.ActivityStudentProfileBinding

class StudentProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityStudentProfileBinding
    private var progressDialog: Dialog? = null
    private var dialogText: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityStudentProfileBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val extras = intent.extras
        val phone = extras?.getString("Number").toString()
        progressDialog = Dialog(this)
        progressDialog!!.setContentView(R.layout.dialog_layout)
        progressDialog!!.setCancelable(false)
        progressDialog!!.getWindow()!!
            .setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        dialogText = progressDialog!!.findViewById(R.id.dialog_text)
        dialogText!!.setText("Loading")
        getDetails(phone)

        binding.number.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                // You already have permission, so call the phone number
                val phoneNumber = binding.number.text.toString() // Replace with the phone number you want to call
                val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:$phoneNumber"))
                startActivity(intent)
            } else {
                // You don't have permission, so request it
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CALL_PHONE), REQUEST_CALL_PHONE_PERMISSION)
            }

        }
        binding.number2.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                // You already have permission, so call the phone number
                val phoneNumber = binding.number2.text.toString() // Replace with the phone number you want to call
                val intent = Intent(Intent.ACTION_CALL, Uri.parse("tel:$phoneNumber"))
                startActivity(intent)
            } else {
                // You don't have permission, so request it
                ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.CALL_PHONE), REQUEST_CALL_PHONE_PERMISSION)
            }

        }
    }

    fun getDetails(no: String) {
        progressDialog!!.show()
        val callback = object : FirebaseQuery.FirestoreCallback2 {
            override fun onDataReceived(data: Student) {
                // Do something with the data
                Log.d(ContentValues.TAG, "Data received: $data")
                binding.apply {
                    name.text = data.NAME
                    email.text = data.EMAIL_ID
                    education.text = data.EDUCATION
                    dob.text = data.DOB
                    occ.text = data.OCCUPATION
                    insta.text = data.INSTA_ID
                    add.text = data.ADDRESS
                    clg.text = data.COLLEGE
                    number.text = data.PHONE
                    number2.text = data.PHONE2
                }
            }
        }
        return FirebaseQuery.getStudentProfile(no, callback, object : MyCompleteListener {
            override fun onSuccess() {
                progressDialog!!.dismiss()
                Toast.makeText(this@StudentProfileActivity, "Success", Toast.LENGTH_SHORT).show()
            }

            override fun onFailure() {
                progressDialog!!.dismiss()
                Toast.makeText(this@StudentProfileActivity, "Failure", Toast.LENGTH_SHORT).show()
            }

        })
    }
    companion object {
        private const val REQUEST_CALL_PHONE_PERMISSION = 1
    }
}
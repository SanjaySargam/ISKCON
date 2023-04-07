package com.example.iskcon

import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class StudentRegistrationActivity : AppCompatActivity() {
    private lateinit var name: EditText
    private lateinit var email: EditText
    private lateinit var phone: EditText
    private lateinit var address: EditText
    private lateinit var college: EditText
    private lateinit var dob: EditText
    private lateinit var insta: EditText
    private lateinit var education: EditText
    private lateinit var occupation: EditText
    private lateinit var addBtn: Button
    private var progressDialog: Dialog? = null
    private var dialogText: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_registration)
        name = findViewById(R.id.etName)
        email = findViewById(R.id.etEmail)
        phone = findViewById(R.id.etNumber)
        address = findViewById(R.id.etAddress)
        college = findViewById(R.id.etCollege)
        dob = findViewById(R.id.etDOB)
        insta = findViewById(R.id.etInsta)
        education = findViewById(R.id.etEducation)
        occupation = findViewById(R.id.etOccupation)
        addBtn = findViewById(R.id.btnAdd)



        progressDialog = Dialog(this)
        progressDialog!!.setContentView(R.layout.dialog_layout)
        progressDialog!!.setCancelable(false)
        progressDialog!!.getWindow()!!
            .setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        dialogText = progressDialog!!.findViewById(R.id.dialog_text)
        dialogText!!.setText("Registering Student")

        addBtn.setOnClickListener {
            if (validate(name, email, phone, address, college, dob, insta, education, occupation)) {
                progressDialog!!.show()
                val devoteeName = name.text.toString()
                val emailStr = email.text.toString()
                val mobile_number = phone.text.toString()
                val addressStr = address.text.toString()
                val collegeStr = college.text.toString()
                val DOBStr = dob.text.toString()
                val instaStr = insta.text.toString()
                val educationStr = education.text.toString()
                val occupationStr = occupation.text.toString()
                FirebaseQuery.createStudent(
                    devoteeName,
                    emailStr,
                    mobile_number,
                    addressStr,
                    collegeStr,
                    DOBStr,
                    instaStr,
                    educationStr,
                    occupationStr,
                    object : MyCompleteListener {
                        override fun onSuccess() {
                            clear(
                                name,
                                email,
                                phone,
                                address,
                                college,
                                dob,
                                insta,
                                education,
                                occupation
                            )
                            progressDialog!!.dismiss()
                            Toast.makeText(baseContext, "Success", Toast.LENGTH_SHORT).show()
//                        FirebaseQuery.addStudentToPreacher(devoteeName,emailStr,mobile_number,addressStr,collegeStr,DOBStr,instaStr,educationStr,occupationStr,object : MyCompleteListener{
//                            override fun onSuccess() {
//                                progressDialog!!.dismiss()
//                                Toast.makeText(baseContext,"Data added successfully", Toast.LENGTH_SHORT).show()
//                            }
//
//                            override fun onFailure() {
//                                progressDialog!!.dismiss()
//                                Toast.makeText(baseContext,"Error 101", Toast.LENGTH_SHORT).show()
//                            }
//
//                        })
                        }

                        override fun onFailure() {
                            progressDialog!!.dismiss()
                            Toast.makeText(baseContext, "Error 101", Toast.LENGTH_SHORT).show()
                        }

                    })
            } else {
                Toast.makeText(baseContext, "Error", Toast.LENGTH_SHORT).show()
            }
        }
    }

    fun validate(
        name: EditText,
        email: EditText,
        number: EditText,
        address: EditText,
        college: EditText,
        DOB: EditText,
        insta: EditText,
        education: EditText,
        occupation: EditText
    ): Boolean {
        val devoteeName = name.text.toString()
        val emailStr = email.text.toString()
        val mobile_number = number.text.toString()
        val addressStr = address.text.toString()
        val collegeStr = college.text.toString()
        val DOBStr = DOB.text.toString()
        val instaStr = insta.text.toString()
        val educationStr = education.text.toString()
        val occupationStr = occupation.text.toString()
        if (devoteeName.isEmpty()) {
            name.error = "Enter name"
            return false
        }
        if (emailStr.isEmpty()) {
            email.error = "Enter email"
            return false
        }
        if (addressStr.isEmpty()) {
            address.error = "Enter address"
            return false
        }
        if (collegeStr.isEmpty()) {
            college.error = "Enter college"
            return false
        }
        if (DOBStr.isEmpty()) {
            DOB.error = "Enter DOB"
            return false
        }
        if (instaStr.isEmpty()) {
            insta.error = "Enter insta-id"
            return false
        }
        if (educationStr.isEmpty()) {
            education.error = "Enter education"
            return false
        }
        if (occupationStr.isEmpty()) {
            occupation.error = "Enter occupation"
            return false
        }
        if (mobile_number.isEmpty()) {
            number.error = "Enter Mobile Number"
            return false
        }
        if (!(mobile_number.length == 10 && TextUtils.isDigitsOnly(mobile_number))) {
            number.error = "Enter Valid Mobile Number"
            return false
        }
        return true
    }

    fun clear(
        name: EditText,
        email: EditText,
        number: EditText,
        address: EditText,
        college: EditText,
        DOB: EditText,
        insta: EditText,
        education: EditText,
        occupation: EditText
    ) {
        name.text.clear()
        number.text.clear()
        email.text.clear()
        address.text.clear()
        college.text.clear()
        DOB.text.clear()
        insta.text.clear()
        education.text.clear()
        occupation.text.clear()
    }

}

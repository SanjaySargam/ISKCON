package com.example.iskcon

import android.app.DatePickerDialog
import android.app.Dialog
import android.media.MediaPlayer
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import android.content.Intent
import java.util.*

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
    private lateinit var phone2: EditText
    private lateinit var addBtn: Button
    private var progressDialog: Dialog? = null
    private var dialogText: TextView? = null
    private lateinit var mediaPlayer: MediaPlayer

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
        phone2 = findViewById(R.id.etPhone2)
        addBtn = findViewById(R.id.btnAdd)
        mediaPlayer = MediaPlayer.create(this, R.raw.sound)

        progressDialog = Dialog(this)
        progressDialog!!.setContentView(R.layout.dialog_layout)
        progressDialog!!.setCancelable(false)
        progressDialog!!.getWindow()!!
            .setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        dialogText = progressDialog!!.findViewById(R.id.dialog_text)
        dialogText!!.setText("Registering Student")

        dob.setOnClickListener {
            val calendar = Calendar.getInstance()
            val year = calendar.get(Calendar.YEAR)
            val month = calendar.get(Calendar.MONTH)
            val day = calendar.get(Calendar.DAY_OF_MONTH)

            val datePickerDialog = DatePickerDialog(
                this,
                { _, year, monthOfYear, dayOfMonth ->
                    // Do something with the selected date
                    // For example, update a TextView with the selected date
                    val selectedDate = "${dayOfMonth}-${monthOfYear + 1}-${year}"
                    val editable = Editable.Factory.getInstance().newEditable(selectedDate)
                    dob.text = editable
                }, year, month, day
            )

            datePickerDialog.show()

        }

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
                val phone2Str = phone2.text.toString()

                // Show the splash screen
                val intent = Intent(this, ticksplashscreen::class.java)
                startActivity(intent)

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
                    phone2Str,
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
                                occupation,
                                phone2
                            )
                            FirebaseQuery.addStudentToPreacher(
                                devoteeName,
                                emailStr,
                                mobile_number,
                                addressStr,
                                collegeStr,
                                DOBStr,
                                instaStr,
                                educationStr,
                                occupationStr,
                                phone2Str,
                                object : MyCompleteListener {
                                    override fun onSuccess() {
                                        progressDialog!!.dismiss()
                                        mediaPlayer.start()
                                        Toast.makeText(
                                            baseContext,
                                            "New Student Added Successfully",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    }

                                    override fun onFailure() {
                                        progressDialog!!.dismiss()
                                        Toast.makeText(baseContext, "Error 101", Toast.LENGTH_SHORT)
                                            .show()
                                    }

                                })
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
        occupation: EditText,
        phone2: EditText
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
        phone2.text.clear()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

}

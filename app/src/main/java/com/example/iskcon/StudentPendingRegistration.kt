package com.example.iskcon

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.ContentValues
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.util.Log
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import java.util.*

class StudentPendingRegistration : AppCompatActivity() {
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
    private lateinit var location: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_pending_registration)
        val extras = intent.extras
        val number = extras?.getString("Number").toString()
        name = findViewById(R.id.etName)
        email = findViewById(R.id.etEmail)
        phone = findViewById(R.id.etNumber)
        address = findViewById(R.id.etAddress)
        college = findViewById(R.id.etCollege)
        education = findViewById(R.id.etEducation)
        dob = findViewById(R.id.etDOB)
        insta = findViewById(R.id.etInsta)
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
        dialogText!!.setText("Pending Details")
        getDetails(number)

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
            if (validate(name, email, phone, address, college, dob, insta,education ,occupation)) {
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
                    "BHIWANDI",
                    object : MyCompleteListener {
                        override fun onSuccess() {
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
                                        val intent = Intent(this@StudentPendingRegistration, ticksplashscreen::class.java)
                                        startActivity(intent)
                                    }

                                    override fun onFailure() {
                                        progressDialog!!.dismiss()
                                    }

                                })
                        }

                        override fun onFailure() {
                            progressDialog!!.dismiss()
                        }

                    })
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


    override fun onDestroy() {
        super.onDestroy()
    }
    fun getDetails(no: String) {
        progressDialog!!.show()
        val callback = object : FirebaseQuery.FirestoreCallback2 {
            override fun onDataReceived(data: Student) {
                // Do something with the data
                Log.d(ContentValues.TAG, "Data received: $data")
                name.setText(data.NAME)
                email.setText(data.EMAIL_ID)
                phone.setText(data.PHONE)
                address.setText(data.ADDRESS)
                college.setText(data.COLLEGE)
                dob.setText(data.DOB)
                insta.setText(data.INSTA_ID)
                occupation.setText(data.OCCUPATION)
                phone2.setText(data.PHONE2)
            }
        }
        return FirebaseQuery.getStudentProfile(no, callback, object : MyCompleteListener {
            override fun onSuccess() {
                progressDialog!!.dismiss()
                Toast.makeText(this@StudentPendingRegistration, "Success", Toast.LENGTH_SHORT).show()
            }

            override fun onFailure() {
                progressDialog!!.dismiss()
                Toast.makeText(this@StudentPendingRegistration, "Failure", Toast.LENGTH_SHORT).show()
            }

        })
    }
}

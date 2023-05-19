package com.example.iskcon

import android.app.DatePickerDialog
import android.app.Dialog
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

class StudentRegistrationActivity : AppCompatActivity() {
    private lateinit var name: EditText
    private lateinit var email: EditText
    private lateinit var phone: EditText
    private lateinit var address: Spinner
    private lateinit var college: EditText
    private lateinit var dob: EditText
    private lateinit var insta: EditText
    private lateinit var education: EditText
    private lateinit var occupation: Spinner
    private lateinit var phone2: EditText
    private lateinit var addBtn: Button
    private var progressDialog: Dialog? = null
    private var dialogText: TextView? = null
    private lateinit var mediaPlayer: MediaPlayer
    private lateinit var location: String

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

        val options = arrayOf("Bhiwandi", "Kalyan", "Others")

        val builder = AlertDialog.Builder(this)
        builder.setTitle("Select a Location")
        builder.setItems(options) { _, which ->
            when (options[which]) {
                "Bhiwandi" -> {
                    // Perform action for Bhiwandi option
                    location = "BHIWANDI"
                    Log.i("dfdfg",location)
                }
                "Kalyan" -> {
                    // Perform action for Kalyan option
                    location = "KALYAN"
                    Log.i("dfdfg",location)

                }
                "Others" -> {
                    // Perform action for Others option
                    location = "OTHERS"
                    Log.i("dfdfg",location)

                }
            }
        }

        val dialog = builder.create()
        dialog.show()

        val spinnerAdapter1 =
            ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mutableListOf())
        spinnerAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerAdapter1.add("Select Address")
        spinnerAdapter1.add("Kamatghar")
        spinnerAdapter1.add("Dhamankar Naka")
        spinnerAdapter1.add("Bhandari Compound")
        spinnerAdapter1.add("Mansarovar")
        address.adapter = spinnerAdapter1
        address.setSelection(0)

        val spinnerAdapter2=
            ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mutableListOf())
        spinnerAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerAdapter2.add("Select Occupation")
        spinnerAdapter2.add("Student")
        spinnerAdapter2.add("Job")
        occupation.adapter = spinnerAdapter2
        occupation.setSelection(0)



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
            Log.i("dsgsdf",location)
            if (validate(name, email, phone, address, college, dob, insta, occupation)) {
                progressDialog!!.show()
                val devoteeName = name.text.toString()
                val emailStr = email.text.toString()
                val mobile_number = phone.text.toString()
                val addressStr = address.selectedItem.toString()
                val collegeStr = college.text.toString()
                val DOBStr = dob.text.toString()
                val instaStr = insta.text.toString()
                val educationStr = education.text.toString()
                val occupationStr = occupation.selectedItem.toString()
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
                    location,
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
                                        val intent = Intent(this@StudentRegistrationActivity, ticksplashscreen::class.java)
                                        startActivity(intent)
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
        address: Spinner,
        college: EditText,
        DOB: EditText,
        insta: EditText,
        occupation: Spinner
    ): Boolean {
        val devoteeName = name.text.toString()
        val emailStr = email.text.toString()
        val mobile_number = number.text.toString()
        val addressStr = address.selectedItem.toString()
        val DOBStr = DOB.text.toString()
        val instaStr = insta.text.toString()
        val occupationStr = occupation.selectedItem.toString()
        if (devoteeName.isEmpty()) {
            name.error = "Enter name"
            return false
        }
        if (occupationStr.isEmpty()) {
            name.error = "Select occupation"
            return false
        }
        if (addressStr.isEmpty()) {
            name.error = "Select Address"
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
        if (DOBStr.isEmpty()) {
            dob.error = "Enter DOB"
            return false
        }
        return true
    }

    fun clear(
        name: EditText,
        email: EditText,
        number: EditText,
        address: Spinner,
        college: EditText,
        DOB: EditText,
        insta: EditText,
        education: EditText,
        occupation: Spinner,
        phone2: EditText
    ) {
        name.text.clear()
        number.text.clear()
        email.text.clear()
        address.setSelection(0)
        college.text.clear()
        DOB.text.clear()
        insta.text.clear()
        education.text.clear()
        occupation.setSelection(0)
        phone2.text.clear()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

}

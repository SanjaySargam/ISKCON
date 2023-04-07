package com.example.iskcon

import android.app.DatePickerDialog
import android.app.Dialog
import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.ViewGroup
import android.widget.*
import com.example.iskcon.databinding.AttendanceFormBinding
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import java.util.*
import kotlin.collections.ArrayList

class TakeAttendanceActivity : AppCompatActivity() {
    private lateinit var binding: AttendanceFormBinding
    private lateinit var studentNo: AutoCompleteTextView
    private lateinit var studentName: AutoCompleteTextView
    private lateinit var isPresentCheckBox: CheckBox
    private lateinit var saveBtn: Button
    private lateinit var selectDate: Button
    private lateinit var databaseReference: DatabaseReference
    private var progressDialog: Dialog? = null
    private var dialogText: TextView? = null
    var numberList = ArrayList<String>()
    var nameList = ArrayList<String>()
    val database = Firebase.database
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        numberList = getMobileNo()
        nameList = getNames()
        binding = AttendanceFormBinding.inflate(layoutInflater)
        setContentView(binding.root)



        Log.i("MOBILE", numberList.toString())
        progressDialog = Dialog(this)
        progressDialog!!.setContentView(R.layout.dialog_layout)
        progressDialog!!.setCancelable(false)
        progressDialog!!.getWindow()!!
            .setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        dialogText = progressDialog!!.findViewById(R.id.dialog_text)
        dialogText!!.setText("Loading")


        val mobile = numberList
        val adapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, mobile)
        val name = nameList
        val namesAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, name)
        studentName = findViewById(R.id.studentName)
        isPresentCheckBox = findViewById(R.id.isPresentCheckBox)
        saveBtn = findViewById(R.id.saveButton)
        studentNo = findViewById(R.id.studentNo)
        selectDate = findViewById(R.id.select_date_button)
        studentNo.setAdapter(adapter)
        studentName.setAdapter(namesAdapter)
        var selectedItem = ""
        studentNo.setOnItemClickListener { parent, view, position, id ->
            progressDialog!!.show()
            selectedItem = parent.getItemAtPosition(position).toString()
            val callback = object : FirebaseQuery.FirestoreCallback {
                override fun onDataReceived(data: String) {
                    // Do something with the data
                    Log.d(TAG, "Data received: $data")
                    val editable = Editable.Factory.getInstance().newEditable(data)
                    studentName.text = editable
                }
            }
            // Code to be executed when an item is selected from the spinner
            val name = FirebaseQuery.getName(
                selectedItem,
                callback,
                object : MyCompleteListener {
                    override fun onSuccess() {
                        progressDialog!!.dismiss()
                        Toast.makeText(this@TakeAttendanceActivity, "Success", Toast.LENGTH_SHORT)
                            .show()
                    }

                    override fun onFailure() {
                        progressDialog!!.dismiss()
                        Toast.makeText(this@TakeAttendanceActivity, "Failure", Toast.LENGTH_SHORT)
                            .show()

                    }
                }).toString()

            Log.i("nammm", name.toString())

        }
        studentName.setOnItemClickListener { parent, view, position, id ->
            progressDialog!!.show()
            selectedItem = parent.getItemAtPosition(position).toString()
            val callback = object : FirebaseQuery.FirestoreCallback {
                override fun onDataReceived(data: String) {
                    // Do something with the data
                    Log.d(TAG, "Data received: $data")
                    val editable = Editable.Factory.getInstance().newEditable(data)
                    studentNo.text = editable
                }
            }
            // Code to be executed when an item is selected from the spinner
            val name = FirebaseQuery.getNumber(
                selectedItem,
                callback,
                object : MyCompleteListener {
                    override fun onSuccess() {
                        progressDialog!!.dismiss()
                        Toast.makeText(this@TakeAttendanceActivity, "Success", Toast.LENGTH_SHORT)
                            .show()
                    }

                    override fun onFailure() {
                        progressDialog!!.dismiss()
                        Toast.makeText(this@TakeAttendanceActivity, "Failure", Toast.LENGTH_SHORT)
                            .show()

                    }
                }).toString()

            Log.i("nammm", name.toString())

        }
        saveBtn.setOnClickListener {
            if (validate()) {
                val attendanceRecord = AttendanceRecord(
                    selectedItem,
                    studentName.text.toString(),
                    isPresentCheckBox.isChecked
                )
                addAttendanceRecord(attendanceRecord)
                clear()
            }
        }
        selectDate.setOnClickListener {
            onSelectDateButtonClick()
        }


    }

    fun clear() {
        studentNo.text.clear()
        studentName.text.clear()
    }

    fun validate(): Boolean {
        if (studentNo.text.isEmpty()) {
            studentNo.setError("Enter Mobile Number")
            return false
        }
        if (studentName.text.isEmpty()) {
            studentName.setError("Enter Student Name")
            return false
        }
        if (selectDate.text == "Select Date") {
            selectDate.setError("Select Date")
            return false
        }
        return true
    }

    fun addAttendanceRecord(record: AttendanceRecord) {
        databaseReference.child(record.studentNo).setValue(record)
            .addOnSuccessListener {
                Toast.makeText(this, "Attendance record added successfully", Toast.LENGTH_SHORT)
                    .show()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to add attendance record", Toast.LENGTH_SHORT).show()
            }
    }

    fun getMobileNo(): ArrayList<String> {
        progressDialog?.show()
        return FirebaseQuery.getMobileNumber(object : MyCompleteListener {
            override fun onSuccess() {
                progressDialog?.dismiss()
                Toast.makeText(this@TakeAttendanceActivity, "Success", Toast.LENGTH_SHORT).show()
            }

            override fun onFailure() {
                progressDialog?.dismiss()
                Toast.makeText(this@TakeAttendanceActivity, "Failure", Toast.LENGTH_SHORT).show()
            }

        })
    }

    fun getNames(): ArrayList<String> {
        progressDialog?.show()
        return FirebaseQuery.getNames(object : MyCompleteListener {
            override fun onSuccess() {
                progressDialog?.dismiss()
                Toast.makeText(this@TakeAttendanceActivity, "Success", Toast.LENGTH_SHORT).show()
            }

            override fun onFailure() {
                progressDialog?.dismiss()
                Toast.makeText(this@TakeAttendanceActivity, "Failure", Toast.LENGTH_SHORT).show()
            }

        })
    }

    fun onSelectDateButtonClick() {
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
                createDate(selectedDate)
                selectDate.text = selectedDate
            }, year, month, day
        )

        datePickerDialog.show()
    }

    fun createDate(date: String) {
        databaseReference = database.getReference("attendance").child(date)
        databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                if (dataSnapshot.exists()) {
                    return
                    // Child node exists
                    // Do something here
                } else {
                    // Child node does not exist
                    // Do something else here
                    FirebaseQuery.firestore?.collection("STUDENTS")
                        ?.addSnapshotListener { snapshot, exception ->
                            if (exception != null) {
                                // Handle the exception
                                return@addSnapshotListener
                            }

                            snapshot?.forEach { document ->
                                val studentNo = document.getString("PHONE").toString()
                                val studentName = document.getString("NAME").toString()
                                databaseReference.child(studentNo)
                                    .setValue(AttendanceRecord(studentNo, studentName, false))
                            }
                        }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle error here
            }
        })


    }

    fun setRecyclerView() {

    }
//fun getName(no:String):Editable{
//    lateinit var editable:Editable
//    progressDialog?.show()
//    val name=FirebaseQuery.getName(no)
//    Log.i("bdsjf",name)
//    editable = Editable.Factory.getInstance().newEditable(name)
//    return editable
//}
}
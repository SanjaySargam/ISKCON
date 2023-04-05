package com.example.iskcon

import android.app.Dialog
import android.content.ContentValues.TAG
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.util.Log
import android.view.ViewGroup
import android.widget.*
import com.example.iskcon.databinding.ActivityMainBinding
import com.example.iskcon.databinding.AttendanceFormBinding

class TakeAttendanceActivity : AppCompatActivity() {
    private lateinit var binding: AttendanceFormBinding
    private var progressDialog: Dialog? = null
    private var dialogText: TextView? = null
    var numberList = ArrayList<String>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        numberList = getMobileNo()
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
        val studentName = findViewById<EditText>(R.id.studentName)
        val isPresentCheckBox = findViewById<CheckBox>(R.id.isPresentCheckBox)
        val saveButton = findViewById<Button>(R.id.saveButton)
        val autoCompleteTextView = findViewById<AutoCompleteTextView>(R.id.studentNo)
        autoCompleteTextView.setAdapter(adapter)
        autoCompleteTextView.setOnItemClickListener { parent, view, position, id ->
            progressDialog!!.show()
            val selectedItem = parent.getItemAtPosition(position)
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
                selectedItem.toString(),
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
//fun getName(no:String):Editable{
//    lateinit var editable:Editable
//    progressDialog?.show()
//    val name=FirebaseQuery.getName(no)
//    Log.i("bdsjf",name)
//    editable = Editable.Factory.getInstance().newEditable(name)
//    return editable
//}
}
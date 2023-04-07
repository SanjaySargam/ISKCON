package com.example.iskcon

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.epoxy.EpoxyRecyclerView
import com.google.firebase.database.*


class AttendanceRecordActivity : AppCompatActivity() {
    private lateinit var recordRv:EpoxyRecyclerView
    private lateinit var controller: AttendanceController
    private lateinit var selectDate:Spinner
    private lateinit var isPresent:Spinner
    private lateinit var databaseReference:DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_attendance_record)
        databaseReference = FirebaseDatabase.getInstance().getReference("attendance")
        recordRv=findViewById(R.id.record_rv)
        setRecycler()

        selectDate = findViewById(R.id.selectDate)
        val spinnerAdapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mutableListOf())
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        getDates(spinnerAdapter,databaseReference)
        selectDate.adapter = spinnerAdapter
        selectDate.setSelection(0)



        selectDate.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                // Handle the selection event here
                val selectedItem = parent?.getItemAtPosition(position) as String
                if (position != 0){
                    getStudentsList(selectedItem)
//                    val dataList=ArrayList<AttendanceRecord>()
//                    dataList.clear()
//                    dataList.add(AttendanceRecord("9370868033","Sanjay",true))
//                    controller.setData(dataList)
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle the case where no item is selected
            }
        }
//        selectDate.setOnItemClickListener { parent, view, position, id ->
//
//        }


        isPresent=findViewById(R.id.is_present)
        val spinnerValues = listOf("Status", "Present", "Absent")
        val adapter = ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, spinnerValues)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        isPresent.adapter = adapter
        isPresent.setSelection(0)
        isPresent.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                // Handle the selection event here
                if (isDateSelected()){
                    val selectedItem = parent?.getItemAtPosition(position) as String
                    val date=selectDate.selectedItem.toString()
                    if (position != 0){
                        val isPresent= selectedItem=="Present"
                        getStudentsPA(date,isPresent)
//                    val dataList=ArrayList<AttendanceRecord>()
//                    dataList.clear()
//                    dataList.add(AttendanceRecord("9370868033","Sanjay",true))
//                    controller.setData(dataList)
                    }
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Handle the case where no item is selected
            }
        }



    }
    fun isDateSelected():Boolean{
        if (selectDate.selectedItem.toString()=="Select Date"){
            (selectDate.selectedView as? TextView)?.error="Select Date"
            return false
        }
        return true
    }
    fun getStudentsList(date:String){
        val dataList=ArrayList<AttendanceRecord>()
        dataList.clear()
        Log.i("fgds",date)
        databaseReference.child(date)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (document in snapshot.children){
                        val studentNo=document.child("studentNo").getValue(String::class.java).toString()
                        val studentName=document.child("studentName").getValue(String::class.java).toString()
                        val isPresent= document.child("present").getValue(Boolean::class.java)
                        isPresent?.let { AttendanceRecord(studentNo,studentName, it) }
                            ?.let { dataList.add(it) }
                    }
                    controller.setData(dataList!!)                }

                override fun onCancelled(error: DatabaseError) {
                }

            })
    }
    fun getStudentsPA(date:String,isPresent:Boolean){
        val dataList=ArrayList<AttendanceRecord>()
        dataList.clear()
        Log.i("fgds",date)
        databaseReference.child(date)
            .orderByChild("present").equalTo(isPresent)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    for (document in snapshot.children){
                        val studentNo=document.child("studentNo").getValue(String::class.java).toString()
                        val studentName=document.child("studentName").getValue(String::class.java).toString()
                        val isPresent= document.child("present").getValue(Boolean::class.java)
                        isPresent?.let { AttendanceRecord(studentNo,studentName, it) }
                            ?.let { dataList.add(it) }
                    }
                    controller.setData(dataList!!)                }

                override fun onCancelled(error: DatabaseError) {
                }

            })
    }
    fun setRecycler(){
        recordRv.apply {
            layoutManager = LinearLayoutManager(context)
        }
        controller= AttendanceController(this)
        recordRv.setController(controller)
//        val dataList=ArrayList<AttendanceRecord>()
//        dataList.clear()
//        dataList.add(AttendanceRecord("9370868033","Sanjay",true))
//        controller.setData(dataList)
    }
    private fun getDates(spinnerAdapter:ArrayAdapter<String>,databaseReference: DatabaseReference) {
        spinnerAdapter.add("Select Date")
        databaseReference.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                val childName = snapshot.key // retrieve the child name
                // add the child name to the spinner adapter
                spinnerAdapter.add(childName)
                spinnerAdapter.notifyDataSetChanged()
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onChildRemoved(snapshot: DataSnapshot) {}

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onCancelled(error: DatabaseError) {}
        })
    }

}
package com.example.iskcon

import android.app.Dialog
import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.airbnb.epoxy.EpoxyRecyclerView

class PendingActivity : AppCompatActivity() {
    private lateinit var pendingRv: EpoxyRecyclerView
    private lateinit var controller: PendingController
    private var progressDialog: Dialog? = null
    private var dialogText: TextView? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pending)
        pendingRv = findViewById(R.id.pending_rv)
        progressDialog = Dialog(this)
        progressDialog!!.setContentView(R.layout.dialog_layout)
        progressDialog!!.setCancelable(false)
        progressDialog!!.getWindow()!!
            .setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        dialogText = progressDialog!!.findViewById(R.id.dialog_text)
        dialogText!!.setText("Loading")
        setRecycler()
        getDetailss()
    }
    fun setRecycler() {
        pendingRv.apply {
            layoutManager = LinearLayoutManager(context)
        }
        controller = PendingController(this)
        pendingRv.setController(controller)
//        val dataList=ArrayList<AttendanceRecord>()
//        dataList.clear()
//        dataList.add(AttendanceRecord("9370868033","Sanjay",true))
//        controller.setData(dataList)
    }

    fun getDetailss() {
        progressDialog!!.show()
        val callback = object : FirebaseQuery.FirestoreCallback4 {

            override fun onDataReceived(data: List<PendingDetails>) {
                controller.setData(data)
            }
        }
        return FirebaseQuery.getAllStudentDetails(callback, object : MyCompleteListener {
            override fun onSuccess() {
                progressDialog!!.dismiss()
            }

            override fun onFailure() {
                progressDialog!!.dismiss()
            }

        })
    }

}
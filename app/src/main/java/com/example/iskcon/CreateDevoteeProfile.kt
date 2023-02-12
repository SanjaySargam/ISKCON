package com.example.iskcon

import android.app.Dialog
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment

class CreateDevoteeProfile : Fragment() {
    private lateinit var name:EditText
    private lateinit var number:EditText
    private lateinit var addBtn:Button
    private var progressDialog: Dialog? = null
    private var dialogText: TextView? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v=inflater.inflate(R.layout.fragment_create_devotee_profile, container, false)

        name=v.findViewById(R.id.etName)
        number=v.findViewById(R.id.etNumber)
        addBtn=v.findViewById(R.id.btnAdd)

        progressDialog = Dialog(requireContext())
        progressDialog!!.setContentView(R.layout.dialog_layout)
        progressDialog!!.setCancelable(false)
        progressDialog!!.getWindow()!!
            .setLayout(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)

        dialogText = progressDialog!!.findViewById<TextView>(R.id.dialog_text)
        dialogText!!.setText("Adding Devotee")

        addBtn.setOnClickListener {
            if (validate(name,number)){
                progressDialog!!.show()
                val devoteeName=name.text.toString()
                val mobile_number=number.text.toString()
                FirebaseQuery.createDevotee(devoteeName,mobile_number,object : MyCompleteListener{
                    override fun onSuccess() {
                        progressDialog!!.dismiss()
                        clear(name,number)
                        Toast.makeText(context,"Data added successfully",Toast.LENGTH_SHORT).show()
                    }

                    override fun onFailure() {
                        progressDialog!!.dismiss()
                        Toast.makeText(context,"Error 101",Toast.LENGTH_SHORT).show()
                    }

                })
            }
            else{
                Toast.makeText(context,"Error",Toast.LENGTH_SHORT).show()
            }
        }

        // Inflate the layout for this fragment
        return v;
    }




}
fun validate(name:EditText,number: EditText):Boolean{
    val devoteeName=name.text.toString()
    val mobile_number=number.text.toString()
    if (devoteeName.isEmpty()){
        name.setError("Enter name")
        return false
    }
    if (mobile_number.isEmpty()){
        number.setError("Enter Mobile Number")
        return false
    }
    if (!(mobile_number.length == 10 && TextUtils.isDigitsOnly(mobile_number))){
        number.setError("Enter Valid Mobile Number")
        return false
    }
    return true
}
fun clear(name:EditText,number: EditText){
    name.text.clear()
    number.text.clear()
}
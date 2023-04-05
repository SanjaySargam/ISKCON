package com.example.iskcon

import android.content.ContentValues.TAG
import android.text.Editable
import android.util.ArrayMap
import android.util.Log
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*

var preacher: PreacherModel = PreacherModel("NA", "NA", "NA", "NA","0")
object FirebaseQuery {
    var firestore: FirebaseFirestore? = null

    fun createStudent(name:String,email:String,number:String,address:String,college:String,dob:String,insta:String,education:String,occupation:String,completeListener: MyCompleteListener){
        val devoteeData: MutableMap<String, Any> = ArrayMap()
        devoteeData["NAME"] = name
        devoteeData["EMAIL-ID"]=email
        devoteeData["PHONE"] = number
        devoteeData["ADDRESS"] = address
        devoteeData["COLLEGE"] = college
        devoteeData["DOB"] = dob
        devoteeData["INSTA-ID"] = insta
        devoteeData["EDUCATION"] = education
        devoteeData["OCCUPATION"] = occupation

        val userDoc: DocumentReference? =
            firestore?.collection("STUDENTS")?.document(
                number
            )

        val batch: WriteBatch? = firestore?.batch()
        batch?.set(userDoc!!, devoteeData)

        batch?.commit()?.addOnSuccessListener {
            completeListener.onSuccess()

        }?.addOnFailureListener { completeListener.onFailure() }
    }
    fun addStudentToPreacher(name:String,email:String,number:String,address:String,college:String,dob:String,insta:String,education:String,occupation:String,completeListener: MyCompleteListener){
        val userDoc =
            FirebaseAuth.getInstance().uid?.let {
                firestore?.collection("PREACHERS")?.document(it)
            }
        userDoc?.collection("STUDENTS")?.document(number)
            ?.set(
                StudentRegistrationActivity.Student(
                    name,
                    email,
                    number,
                    address,
                    college,
                    dob,
                    insta,
                    education,
                    occupation
                )
            )
            ?.addOnCompleteListener {
                if (it.isSuccessful) {
                    // Increment the field value by 1
                    val increment = FieldValue.increment(1)

                    // Update the field value
                    userDoc.update("STUDENTS_ENROLLED", increment)
                        .addOnSuccessListener {
                            // Field value incremented successfully
                        }
                        .addOnFailureListener { e ->
                            // Handle any errors that occurred while incrementing the field value
                        }

                    Log.d(TAG, "Data added to Firestore ${it.result}")
                } else {
                    Log.d(TAG, "Data added to Firestore ${it.exception}")
                }
            }
    }

    fun getPreacherData(completeListener: MyCompleteListener) {
        FirebaseAuth.getInstance().uid?.let {
            firestore?.collection("PREACHERS")
                ?.document(it)
                ?.get()
                ?.addOnSuccessListener(OnSuccessListener<DocumentSnapshot> { documentSnapshot: DocumentSnapshot ->
                    preacher.name=documentSnapshot.getString("NAME").toString()
                    preacher.email=documentSnapshot.getString("EMAIL-ID").toString()
                    preacher.phone=documentSnapshot.getString("PHONE").toString()
                    preacher.instaId=documentSnapshot.getString("INSTA").toString()
                    preacher.studentEnrolled=documentSnapshot.getString("STUDENTS_ENROLLED").toString()
                    completeListener.onSuccess()
                })
                ?.addOnFailureListener(OnFailureListener { e: Exception? -> completeListener.onFailure() })
        }
    }
    fun getMobileNumber(completeListener: MyCompleteListener) :ArrayList<String> {
        val mobileNoList = ArrayList<String>()
        firestore?.collection("STUDENTS")?.get()
            ?.addOnSuccessListener { query ->
            for (document in query.documents) {
                val number = document.getString("PHONE")
                number?.let {
                    mobileNoList.add(it)
                }
            }
                completeListener.onSuccess()
        }
        ?.addOnFailureListener { exception ->
            // Handle any errors that occur
            completeListener.onFailure()
        }
        return mobileNoList
    }
//    fun getName(no:String):String{
//        val myCollectionRef = firestore?.collection("STUDENTS")?.document(no)
//        var data = ""
//
//        myCollectionRef?.get()?.addOnSuccessListener { querySnapshot ->
//            // Get the data from the query snapshot and return it as a string
//            data=querySnapshot.getString("NAME").toString()
//        }
//        Log.i("fkjhgj",data)
//        return data
//
////        var name= String()
////        firestore?.collection("STUDENTS")
////            ?.document(no)
////            ?.get()
////            ?.addOnSuccessListener() {
////                name= it.getString("NAME").toString()
////                Log.i("jdjsfh",name)
////                completeListener.onSuccess()
////            }
////            ?.addOnFailureListener {
////                completeListener.onFailure()
////            }
////        return name
//    }
    interface FirestoreCallback {
        fun onDataReceived(data: String)
    }
    fun getName(no:String,callback: FirestoreCallback,completeListener: MyCompleteListener){
        val myCollectionRef = firestore?.collection("STUDENTS")?.document(no)
        myCollectionRef?.get()?.addOnSuccessListener { querySnapshot ->
            // Get the data from the query snapshot and pass it to the callback
            val data = querySnapshot.getString("NAME").toString()
            callback.onDataReceived(data)
            completeListener.onSuccess()
        }
    }


}
package com.example.iskcon

import android.content.ContentValues.TAG
import android.util.ArrayMap
import android.util.Log
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*

var preacher: PreacherModel = PreacherModel("NA", "NA", "NA", "NA", "0")

object FirebaseQuery {
    var firestore: FirebaseFirestore? = null

    fun createStudent(
        name: String,
        email: String,
        number: String,
        address: String,
        college: String,
        dob: String,
        insta: String,
        education: String,
        occupation: String,
        phone2: String,
        location:String,
        completeListener: MyCompleteListener
    ) {
        val devoteeData: MutableMap<String, Any> = ArrayMap()
        devoteeData["NAME"] = name
        devoteeData["EMAIL-ID"] = email
        devoteeData["PHONE"] = number
        devoteeData["ADDRESS"] = address
        devoteeData["COLLEGE"] = college
        devoteeData["DOB"] = dob
        devoteeData["INSTA-ID"] = insta
        devoteeData["EDUCATION"] = education
        devoteeData["OCCUPATION"] = occupation
        devoteeData["PHONE2"] = phone2
        val batch: WriteBatch? = firestore?.batch()

        val userDoc: DocumentReference? =
            firestore?.collection("STUDENTS")?.document(
                number
            )
        if (location!="Others" && location.isNotEmpty()) {
            val userDoc1: DocumentReference? =
                firestore?.collection(location)?.document(
                    number
                )
            batch?.set(userDoc1!!, devoteeData)
        }
        val countDoc: DocumentReference =
            firestore!!.collection("TOTAL_STUDENTS")
                .document("COUNT")
        batch?.set(userDoc!!, devoteeData)
        batch?.update(countDoc, "TOTAL", FieldValue.increment(1))
        batch?.commit()?.addOnSuccessListener {
            completeListener.onSuccess()

        }?.addOnFailureListener { completeListener.onFailure() }
    }

    fun addStudentToPreacher(
        name: String,
        email: String,
        number: String,
        address: String,
        college: String,
        dob: String,
        insta: String,
        education: String,
        occupation: String,
        phone2: String,
        completeListener: MyCompleteListener
    ) {
        val userDoc =
            FirebaseAuth.getInstance().uid?.let {
                firestore?.collection("PREACHERS")?.document(it)
            }
        userDoc?.collection("STUDENTS")?.document(number)
            ?.set(
                Student(
                    name,
                    email,
                    number,
                    address,
                    college,
                    dob,
                    insta,
                    education,
                    occupation,
                    phone2
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
                            completeListener.onSuccess()
                        }
                        .addOnFailureListener { e ->
                            // Handle any errors that occurred while incrementing the field value
                            completeListener.onFailure()
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
                    preacher.name = documentSnapshot.getString("NAME").toString()
                    preacher.email = documentSnapshot.getString("EMAIL-ID").toString()
                    preacher.phone = documentSnapshot.getString("PHONE").toString()
                    preacher.instaId = documentSnapshot.getString("INSTA").toString()
                    preacher.studentEnrolled =
                        documentSnapshot.getString("STUDENTS_ENROLLED").toString()
                    completeListener.onSuccess()
                })
                ?.addOnFailureListener(OnFailureListener { e: Exception? -> completeListener.onFailure() })
        }
    }

    fun getMobileNumber(completeListener: MyCompleteListener): ArrayList<String> {
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

    fun getNames(completeListener: MyCompleteListener): ArrayList<String> {
        val namesList = ArrayList<String>()
        firestore?.collection("STUDENTS")?.get()
            ?.addOnSuccessListener { query ->
                for (document in query.documents) {
                    val number = document.getString("NAME")
                    number?.let {
                        namesList.add(it)
                    }
                }
                completeListener.onSuccess()
            }
            ?.addOnFailureListener { exception ->
                // Handle any errors that occur
                completeListener.onFailure()
            }
        return namesList
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

    interface FirestoreCallback2 {
        fun onDataReceived(data: Student)
    }
    interface FirestoreCallback3 {
        fun onDataReceived(data:Int)
    }

    fun getName(no: String, callback: FirestoreCallback, completeListener: MyCompleteListener) {
        val myCollectionRef = firestore?.collection("STUDENTS")?.document(no)
        myCollectionRef?.get()?.addOnSuccessListener { querySnapshot ->
            // Get the data from the query snapshot and pass it to the callback
            val data = querySnapshot.getString("NAME").toString()
            callback.onDataReceived(data)
            completeListener.onSuccess()
        }
    }

    fun getNumber(name: String, callback: FirestoreCallback, completeListener: MyCompleteListener) {
        val collectionRef = firestore?.collection("STUDENTS")

// Searching for documents where fieldName == "searchValue"
        val query = collectionRef?.whereEqualTo("NAME", name)

// Retrieving the matching documents
        query?.get()?.addOnSuccessListener { documents ->
            for (document in documents) {
                // Do something with the matching documents
                val data = document.getString("PHONE").toString()
                callback.onDataReceived(data)
                Log.d("Firestore", "Document data: $data")
            }
            completeListener.onSuccess()
        }?.addOnFailureListener { exception ->
            Log.w("Firestore", "Error getting documents: ", exception)
            completeListener.onFailure()
        }

    }

    fun getStudentProfile(
        no: String,
        callback: FirestoreCallback2,
        completeListener: MyCompleteListener
    ) {
        firestore?.collection("STUDENTS")
            ?.document(no)
            ?.get()
            ?.addOnSuccessListener {
                val add = it.getString("ADDRESS").toString()
                val clg = it.getString("COLLEGE").toString()
                val dob = it.getString("DOB").toString()
                val ed = it.getString("EDUCATION").toString()
                val email = it.getString("EMAIL-ID").toString()
                val insta = it.getString("INSTA-ID").toString()
                val name = it.getString("NAME").toString()
                val occ = it.getString("OCCUPATION").toString()
                val phone = it.getString("PHONE").toString()
                val phone2 = it.getString("PHONE2").toString()
                callback.onDataReceived(
                    Student(
                        name,
                        email,
                        phone,
                        add,
                        clg,
                        dob,
                        insta,
                        ed,
                        occ,
                        phone2
                    )
                )
                completeListener.onSuccess()
            }
            ?.addOnFailureListener {
                completeListener.onFailure()
            }
    }
    fun getTotal(callback: FirestoreCallback3,completeListener: MyCompleteListener):Int{
        var count=0
        firestore?.collection("TOTAL_STUDENTS")
            ?.document("COUNT")
            ?.get()
            ?.addOnSuccessListener {
                count = it.getLong("TOTAL").toString().toInt()
                callback.onDataReceived(count)
                completeListener.onSuccess()
            }
            ?.addOnFailureListener {
                completeListener.onFailure()
            }
        return count
    }


}
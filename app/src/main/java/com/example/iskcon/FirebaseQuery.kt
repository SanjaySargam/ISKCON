package com.example.iskcon

import android.util.ArrayMap
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.WriteBatch

object FirebaseQuery {
    var firestore: FirebaseFirestore? = null

    fun createDevotee(name:String,number:String,completeListener: MyCompleteListener){
        val devoteeData: MutableMap<String, Any> = ArrayMap()
        devoteeData["NAME"] = name
        devoteeData["MOBILE_NUMBER"] = number

        val userDoc: DocumentReference? =
            firestore?.collection("DEVOTEES")?.document(
                name
            )

        val batch: WriteBatch? = firestore?.batch()
        batch?.set(userDoc!!, devoteeData)

        val countDoc: DocumentReference? =
            firestore?.collection("DEVOTEES")
                ?.document("TOTAL_DEVOTEES")

        if (countDoc != null) {
            batch?.update(countDoc, "COUNT", FieldValue.increment(1) )
        }
        batch?.commit()?.addOnSuccessListener { completeListener.onSuccess() }?.addOnFailureListener { completeListener.onFailure() }
    }

}
package com.example.iskcon

import android.content.Context
import com.airbnb.epoxy.TypedEpoxyController
import com.google.firebase.database.FirebaseDatabase

class AttendanceController(private val context: AttendanceRecordActivity) : TypedEpoxyController<List<AttendanceRecord>>() {
    override fun buildModels(data: List<AttendanceRecord>) {
        // Add the attendance entry form to the RecyclerView

        // Add the attendance records to the RecyclerView
        data.forEach { attendanceRecord ->
            attendanceRecord(context){
                id(attendanceRecord.hashCode())
                attendanceRecord(attendanceRecord)
            }
        }
    }
}

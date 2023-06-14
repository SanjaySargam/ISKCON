package com.example.iskcon

class AttendanceRecord(
    val studentNo: String,
    val studentName: String,
    val isPresent: Boolean
)

class PendingDetails(
    val studentName: String,
    val studentNo: String,
    val pending: List<String>
)
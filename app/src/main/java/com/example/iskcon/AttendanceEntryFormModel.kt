package com.example.iskcon

import android.view.View
import android.view.ViewParent
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder

@EpoxyModelClass
abstract class AttendanceEntryFormModel :
    EpoxyModelWithHolder<AttendanceEntryFormModel.ViewHolder>() {
    @EpoxyAttribute
    lateinit var onSaveAttendanceRecord: (devoteeName: String, date: String, isPresent: Boolean) -> Unit

    override fun getDefaultLayout(): Int {
        return R.layout.attendance_form
    }

    override fun createNewHolder(parent: ViewParent): ViewHolder {
        TODO("Not yet implemented")
    }

    override fun bind(holder: ViewHolder) {
        holder.saveButton.setOnClickListener {
            val studentNo = holder.studentNo.text.toString()
            val studentName = holder.studentName.text.toString()
            val isPresent = holder.isPresentCheckBox.isChecked
            onSaveAttendanceRecord(studentNo, studentName, isPresent)
        }
    }

    inner class ViewHolder : EpoxyHolder() {
        lateinit var studentNo: EditText
        lateinit var studentName: EditText
        lateinit var isPresentCheckBox: CheckBox
        lateinit var saveButton: Button

        override fun bindView(itemView: View) {
            studentNo = itemView.findViewById(R.id.studentNo)
            studentName = itemView.findViewById(R.id.studentName)
            isPresentCheckBox = itemView.findViewById(R.id.isPresentCheckBox)
            saveButton = itemView.findViewById(R.id.saveButton)
        }
    }
}

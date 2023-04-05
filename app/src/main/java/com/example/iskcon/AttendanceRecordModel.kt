package com.example.iskcon

import android.view.View
import android.view.ViewParent
import android.widget.CheckBox
import android.widget.TextView
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder

@EpoxyModelClass
abstract class AttendanceRecordModel : EpoxyModelWithHolder<AttendanceRecordModel.ViewHolder>() {
    @EpoxyAttribute
    lateinit var attendanceRecord: AttendanceRecord

    override fun getDefaultLayout(): Int {
        return R.layout.attendance_record_item_view
    }

    override fun createNewHolder(parent: ViewParent): ViewHolder {
        TODO("Not yet implemented")
    }

    override fun bind(holder: ViewHolder) {
        holder.studentNo.text = attendanceRecord.studentNo
        holder.studentName.text = attendanceRecord.studentName
        holder.isPresentCheckBox.isChecked = attendanceRecord.isPresent
    }

    inner class ViewHolder : EpoxyHolder() {
        lateinit var studentNo: TextView
        lateinit var studentName: TextView
        lateinit var isPresentCheckBox: CheckBox

        override fun bindView(itemView: View) {
            studentNo = itemView.findViewById(R.id.studentNo)
            studentName = itemView.findViewById(R.id.studentName)
            isPresentCheckBox = itemView.findViewById(R.id.isPresentCheckBox)
        }
    }
}

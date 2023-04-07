package com.example.iskcon

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder

@EpoxyModelClass
abstract class AttendanceRecordModel(private val context: Context) :
    EpoxyModelWithHolder<AttendanceRecordModel.ViewHolder>() {
    @EpoxyAttribute
    lateinit var attendanceRecord: AttendanceRecord

    override fun getDefaultLayout(): Int {
        return R.layout.attendance_record_item_view
    }


    override fun bind(holder: ViewHolder) {
        holder.studentNo.text = attendanceRecord.studentNo
        holder.studentName.text = attendanceRecord.studentName
        holder.isPresentCheckBox.isVisible = attendanceRecord.isPresent

        holder.constraintLayout.setOnClickListener {
            val intent = Intent(context, StudentProfileActivity::class.java)
            intent.putExtra("Number", attendanceRecord.studentNo)
            context.startActivity(intent)
        }


    }

    inner class ViewHolder : EpoxyHolder() {
        lateinit var studentNo: TextView
        lateinit var studentName: TextView
        lateinit var isPresentCheckBox: ImageView
        lateinit var constraintLayout: ConstraintLayout

        override fun bindView(itemView: View) {
            studentNo = itemView.findViewById(R.id.studentNo)
            studentName = itemView.findViewById(R.id.studentName)
            isPresentCheckBox = itemView.findViewById(R.id.presentImageView)
            constraintLayout = itemView.findViewById(R.id.record_constraint)
        }
    }
}

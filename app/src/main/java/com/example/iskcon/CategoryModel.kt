package com.example.iskcon

import android.content.Context
import android.content.Intent
import android.view.View
import android.view.ViewParent
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder

@EpoxyModelClass()
abstract class CategoryModel(private val context: Context) :
    EpoxyModelWithHolder<CategoryModel.ViewHolder>() {

    @EpoxyAttribute
    lateinit var data: String

    override fun getDefaultLayout(): Int {
        return R.layout.category_list
    }

    override fun bind(holder: ViewHolder) {
        super.bind(holder)
        holder.category.text = data

        holder.card_view.setOnClickListener {
            if (data == "New Student") {
                moveToStudentRegistration()
            } else if (data == "Take Attendance") {
                moveToTakeAttendance()
            } else if (data == "Attendance Record") {
                moveToAttendanceRecord()
            } else if (data == "Feedback") {
                moveTofeedbacksplash2()
            } else if (data == "Pending Student Details") {
                moveToPendingDetails()
            }
        }


    }

    fun moveToPendingDetails() {
        val intent = Intent(context, PendingActivity::class.java)
        context.startActivity(intent)
    }

    fun moveToAttendanceRecord() {
        val intent = Intent(context, AttendanceRecordActivity::class.java)
        context.startActivity(intent)
    }

    fun moveToTakeAttendance() {
        val intent = Intent(context, TakeAttendanceActivity::class.java)
        context.startActivity(intent)
    }

    fun moveToStudentRegistration() {
        val intent = Intent(context, StudentRegistrationActivity::class.java)
        context.startActivity(intent)
    }

    fun moveTofeedbacksplash2() {
        val intent = Intent(context, feedbacksplash2::class.java)
        context.startActivity(intent)
    }

    inner class ViewHolder : EpoxyHolder() {
        lateinit var category: TextView
        lateinit var card_view: CardView

        override fun bindView(itemView: View) {
            category = itemView.findViewById(R.id.catName)
            card_view = itemView.findViewById(R.id.cardView)
        }
    }
}
package com.example.iskcon

import android.content.Context
import android.content.Intent
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.isVisible
import com.airbnb.epoxy.EpoxyAttribute
import com.airbnb.epoxy.EpoxyHolder
import com.airbnb.epoxy.EpoxyModelClass
import com.airbnb.epoxy.EpoxyModelWithHolder

@EpoxyModelClass
abstract class PendingModel(private val context: Context) :
    EpoxyModelWithHolder<PendingModel.ViewHolder>() {
    @EpoxyAttribute
    lateinit var pendingRecord: PendingDetails

    override fun getDefaultLayout(): Int {
        return R.layout.pending_layout
    }
    override fun bind(holder: ViewHolder) {
        holder.name.text = pendingRecord.studentName
        holder.phone.text = pendingRecord.studentNo
        holder.pendingDetails.text = pendingRecord.pending.toString()

        holder.cardView.setOnClickListener {
            val intent = Intent(context, StudentPendingRegistration::class.java)
            intent.putExtra("Number", pendingRecord.studentNo)
            context.startActivity(intent)
        }


    }

    inner class ViewHolder : EpoxyHolder() {
        lateinit var name: TextView
        lateinit var phone: TextView
        lateinit var pendingDetails: TextView
        lateinit var cardView: CardView


        override fun bindView(itemView: View) {
            name=itemView.findViewById(R.id.name)
            phone=itemView.findViewById(R.id.phone)
            pendingDetails=itemView.findViewById(R.id.pending_details)
            cardView=itemView.findViewById(R.id.cardView1)
        }
    }
}
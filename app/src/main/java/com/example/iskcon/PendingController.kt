package com.example.iskcon

import com.airbnb.epoxy.TypedEpoxyController

class PendingController(private val context: PendingActivity) :
    TypedEpoxyController<List<PendingDetails>>() {
    override fun buildModels(data: List<PendingDetails>) {
        // Add the attendance entry form to the RecyclerView

        // Add the attendance records to the RecyclerView
        data.forEach{ pending ->
            pending(context){
                id(pending.hashCode())
                pendingRecord(pending)
            }
        }
        }
    }
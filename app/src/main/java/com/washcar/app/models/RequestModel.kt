package com.washcar.app.models

import com.google.firebase.firestore.ServerTimestamp
import java.util.*

class RequestModel {

    var requestId: String? = null
    var providerId: String? = null
    var customerId: String? = null
    var customerName: String? = null
    var carName: String? = null
    var carType: String? = null
    var carModel: String? = null
    var carPlateNumber: String? = null
    var destinationLat = 0.0
    var destinationLng = 0.0
    var categoryModels: ArrayList<CategoryModel>? = null

    @ServerTimestamp
    var createdAt: Date? = null
    var requestStatus: String? = null

    companion object {
        const val STATUS_UPCOMING = "upcoming"
        const val STATUS_FINISHED = "finished"
    }
}
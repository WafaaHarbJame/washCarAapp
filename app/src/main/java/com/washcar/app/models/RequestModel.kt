package com.washcar.app.models

import com.google.firebase.firestore.ServerTimestamp
import java.util.*

class RequestModel {

    var requestId: String? = null
    var providerId: String? = null
    var customerId: String? = null
    var customerName: String? = null
    var providerName: String? = null
    var fullName: String? = null
    var carName: String? = null
    var carType: String? = null
    var carModel: String? = null
    var carPlateNumber: String? = null
    var customerLat = 0.0
    var customerLng = 0.0
    var selectedService: String? = null
    var total: Double? = null
    @ServerTimestamp
    var createdAt: Date? = null
    var status:String? = null

    companion object {
        const val STATUS_UPCOMING = "upcoming"
        const val STATUS_FINISHED = "finished"
    }
}
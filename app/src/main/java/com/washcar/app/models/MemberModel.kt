package com.washcar.app.models

import java.io.Serializable

class MemberModel : Serializable {

    var token: String? = null
    var mobile: String? = null
    var type: String? = null
    var fullName: String? = null
    var password: String? = null
    var lat = 0.0
    var lng = 0.0
    var address: String? = null
    var startTime: String? = null
    var endTime: String? = null
    var email: String? = ""
    var description: String? = ""

    companion object {
        const val TYPE_CUSTOMER = "customer"
        const val TYPE_SERVICE_PROVIDER = "service_provider"
        const val TYPE_ADMIN = "admin"
    }


}
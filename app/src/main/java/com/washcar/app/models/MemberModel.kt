package com.washcar.app.models

import java.io.Serializable

class MemberModel : Serializable {

    var id = 0
    var mobile: String? = null
    var type = 0
    var fullName: String? = null
    var password: String? = null
    var passwordConfirm: String? = null
    var lat = 0.0
    var lng = 0.0
    var address: String? = null
    var isSelectLocation: Any? = ""
    var startTime : String? = null
    var endTime : String? = null
    var review: Float? = 0f
    var email: String? = ""
    var description: String? = ""

    companion object{
       const val TYPE_CUSTOMER = "customer"
       const val TYPE_SERVICE_PROVIDER = "service_provider"
    }




    fun getIsSelectLocation(): Boolean {

        if (isSelectLocation is Boolean)
            return isSelectLocation as Boolean
        else if (isSelectLocation is Double) {
            val value = isSelectLocation as Double
            return value.equals(1.0)
        }
        return false
    }


}
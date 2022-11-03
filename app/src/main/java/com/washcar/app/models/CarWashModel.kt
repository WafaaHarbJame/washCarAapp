package com.washcar.app.models

import java.io.Serializable

class CarWashModel : Serializable {
    var lat = 0.0
    var lng = 0.0
    var address: String? = null
    var date: String? = null
    var describtion: String? = null
    var discount = 0
    var type = 0
    var rating = 0.0

}

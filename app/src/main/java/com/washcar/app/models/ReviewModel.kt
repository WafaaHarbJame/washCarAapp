package com.washcar.app.models

import com.google.firebase.firestore.ServerTimestamp
import java.util.*

class ReviewModel {

    var id: String? = null
    var rate: Int? = null
    var comment: String? = null
    var userName: String? = null
    @ServerTimestamp
    var createdAt: Date? = null
    var providerId: String? = null

}
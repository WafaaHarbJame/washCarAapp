package com.washcar.app.models


data class CategoryModel(var id: String? = null, var name: String? = "") {

    var price: Double? = 0.0
    var selected: Boolean? = false

    override fun toString(): String {
        return "$id - $name - $price $selected"
    }
}
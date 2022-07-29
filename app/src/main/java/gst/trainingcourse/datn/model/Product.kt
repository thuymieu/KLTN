package gst.trainingcourse.datn.model

import java.io.Serializable

data class Product(
    var id: Int? = null,
    var id_category: Int? = null,
    var name: String? = null,
    var brand: String? = null,
    var price: Double? = null,
    var photo: String? = null,
    var detail: String? = null,
    var discount: Int? = null,
    var status: String? = null,
    var created_at: String? = null,
    var odo: Int? = null,
    var year_manufacture: Int? = null,
    var number_years_use: Int? = null,
    var color: String? = null
): Serializable
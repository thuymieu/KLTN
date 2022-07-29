package gst.trainingcourse.datn.model

import java.io.Serializable

class ItemProductSold(
    var id_order : Int? = null,
    var username: String? = null,
    var price: Int? = null,
    var name_product: String? = null,
    var image: String? = null,
    var date: String? = null
) : Serializable
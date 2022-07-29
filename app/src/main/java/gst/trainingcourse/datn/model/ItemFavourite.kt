package gst.trainingcourse.datn.model

import java.io.Serializable

data class ItemFavourite (
    var id: Int? = null,
    var product_id: Int? = null,
    var user_id: Int? = null
): Serializable
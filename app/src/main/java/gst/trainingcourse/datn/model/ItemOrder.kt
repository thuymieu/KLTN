package gst.trainingcourse.datn.model

import java.io.Serializable

class ItemOrder(
    var id: Int? = null,
    var order_id: Int? = null,
    var product_id: Int? = null,
    var quantity: Int? = null
): Serializable
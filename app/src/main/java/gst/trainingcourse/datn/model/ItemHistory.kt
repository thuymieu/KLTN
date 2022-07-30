package gst.trainingcourse.datn.model

import java.io.Serializable

class ItemHistory(
    var id:Int? = null,
    var name: String? = null,
    var image: String? = null,
    var price: Double? = null,
    var date: String?=null,
    var status: String?=null
) : Serializable
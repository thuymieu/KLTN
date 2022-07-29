package gst.trainingcourse.datn.model

import java.io.Serializable

data class Category(
    var id: Int? = null,
    var photo: String? = null,
    var category_name: String? = null
): Serializable
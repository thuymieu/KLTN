package gst.trainingcourse.datn.model

import java.io.Serializable

data class News (
    var id: Int? = null,
    var title: String? = null,
    var content: String? = null,
    var created_at: String? = null
): Serializable
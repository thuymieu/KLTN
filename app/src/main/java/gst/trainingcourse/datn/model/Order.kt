package gst.trainingcourse.datn.model

import java.io.Serializable
import java.util.*

class Order (
    var id: Int? = null,
    var user_id: Int? = null,
    var status: String? = null,
    var create_at: String? = null,
    var phone_user: String? = null,
    var address_user: String? = null
): Serializable
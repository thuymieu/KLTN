package gst.trainingcourse.datn.model

import java.io.Serializable


data class User(
    var id : Int? = null,
    var username : String?=null,
    var password : String?=null,
    var phone: String?=null
): Serializable
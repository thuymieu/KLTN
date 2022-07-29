package gst.trainingcourse.datn

import gst.trainingcourse.datn.admin.ui.AdminActivity
import gst.trainingcourse.datn.ui.MainActivity

class Delegate {
    companion object {
        var mainActivity = MainActivity()
        var adminActivity = AdminActivity()
    }
}
package gst.trainingcourse.datn.ui.content

import gst.trainingcourse.datn.model.News

interface IOnNews {
    fun onDetail(news : News)
}
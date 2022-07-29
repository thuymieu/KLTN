package gst.trainingcourse.datn.ui.favorite

import gst.trainingcourse.datn.model.Product

interface IOnFavourite {
    fun onFavourite(p: Product)
    fun onImage(p: Product)
}
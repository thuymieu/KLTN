package gst.trainingcourse.datn.utils

import androidx.recyclerview.widget.DiffUtil
import gst.trainingcourse.datn.model.Product

class ItemProductDiffUtils(private val oldData: List<Product>, private val newData: List<Product>): DiffUtil.Callback() {
    override fun getOldListSize(): Int {
        return oldData.size
    }

    override fun getNewListSize(): Int {
        return newData.size
    }

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldData.getOrNull(oldItemPosition)
        val newItem = newData.getOrNull(newItemPosition)

        if(oldItem != null && newItem != null) {
            return oldItem.id == newItem.id
        }
        return false
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val oldItem = oldData.getOrNull(oldItemPosition)
        val newItem = newData.getOrNull(newItemPosition)
        return oldItem == newItem
    }

}
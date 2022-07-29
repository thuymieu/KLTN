package gst.trainingcourse.datn.data

import androidx.lifecycle.LiveData
import gst.trainingcourse.datn.model.Product

interface IRepository {
    fun getListProduct() : ArrayList<Product>
    suspend fun addProduct(p: Product)
    suspend fun updateProduct(p: Product)
    suspend fun deleteProduct(p: Product)
}
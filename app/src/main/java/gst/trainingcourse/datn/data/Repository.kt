package gst.trainingcourse.datn.data

import android.widget.Toast
import androidx.lifecycle.LiveData
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import gst.trainingcourse.datn.Delegate
import gst.trainingcourse.datn.model.Product

class Repository :IRepository {

    override fun getListProduct(): ArrayList<Product> {
        getListProductFromRealtimeDatabase()
        return getListProductFromRealtimeDatabase()
    }

    override suspend fun addProduct(p: Product) {
    }

    override suspend fun updateProduct(p: Product) {
    }

    override suspend fun deleteProduct(p: Product) {
    }

    private fun getListProductFromRealtimeDatabase() : ArrayList<Product> {
        val myRef = FirebaseDatabase.getInstance().getReference("Products")
        var count = 0
        val listProducts = ArrayList<Product>()
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (dataSnapshot in snapshot.children){
                    count++
                    val product = dataSnapshot.getValue(Product::class.java)
                    listProducts.add(product!!)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(Delegate.mainActivity,listProducts.size.toString(), Toast.LENGTH_LONG).show()
            }

        })
        return listProducts
    }
}
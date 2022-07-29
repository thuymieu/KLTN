package gst.trainingcourse.datn.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import gst.trainingcourse.datn.R
import gst.trainingcourse.datn.databinding.ItemProductInCartBinding
import gst.trainingcourse.datn.model.Product
import gst.trainingcourse.datn.ui.cart.IOnDeleteProduct
import java.text.DecimalFormat

class AdapterProductInCart : RecyclerView.Adapter<AdapterProductInCart.ItemViewHolder>() {
    private val listProduct = arrayListOf<Product>()
    private lateinit var context: Context
    private lateinit var iOnProduct: IOnDeleteProduct
    class ItemViewHolder(
        private val binding: ItemProductInCartBinding,
        private val context: Context,
        private val iOnProduct: IOnDeleteProduct
    ) : RecyclerView.ViewHolder(binding.root) {
        private val pattern = "#,##0 VNĐ"
        private val decimalFormat = DecimalFormat(pattern)
        fun bind(product: Product) {
            binding.imAvatar.setImageResource(R.drawable.ic_launcher_background)
            Glide.with(context).load(product.photo).into(binding.imAvatar)
            binding.tvNameProduct.text = product.name
            binding.tvAmount.text = "1"
            binding.tvPrice.text = decimalFormat.format(product.price?.toInt())
            binding.btnDelete.setOnClickListener {
                iOnProduct.onImage(product)
            }
        }
    }

    fun setIOnProduct(iOnProduct: IOnDeleteProduct){
        this.iOnProduct = iOnProduct
    }
    fun setData(listStudentUpdate: List<Product>, context: Context) {

        this.context = context
        listProduct.clear()
        listProduct.addAll(listStudentUpdate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemProductInCartBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding, context,iOnProduct)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(listProduct[position])
    }

    override fun getItemCount(): Int {
        return listProduct.size
    }
}
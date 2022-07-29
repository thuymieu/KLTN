package gst.trainingcourse.datn.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import gst.trainingcourse.datn.R
import gst.trainingcourse.datn.databinding.ItemFavouriteBinding
import gst.trainingcourse.datn.databinding.ItemProductInCartBinding
import gst.trainingcourse.datn.model.Product
import gst.trainingcourse.datn.ui.cart.IOnDeleteProduct
import gst.trainingcourse.datn.ui.favorite.IOnFavourite
import java.text.DecimalFormat

class AdapterProductInWishlist : RecyclerView.Adapter<AdapterProductInWishlist.ItemViewHolder>() {
    private val listProduct = arrayListOf<Product>()
    private lateinit var context: Context
    private lateinit var iOnProduct: IOnFavourite
    class ItemViewHolder(
        private val binding: ItemFavouriteBinding,
        private val context: Context,
        private val iOnProduct: IOnFavourite
    ) : RecyclerView.ViewHolder(binding.root) {
        private val pattern = "#,##0 VNĐ"
        private val decimalFormat = DecimalFormat(pattern)
        fun bind(product: Product) {
            binding.imProduct.setImageResource(R.drawable.ic_launcher_background)
            Glide.with(context).load(product.photo).into(binding.imProduct)
            binding.tvName.text = product.name
            binding.tvPrice.text = decimalFormat.format(product.price?.toInt())
            binding.iconFavourite.setOnClickListener {
                iOnProduct.onFavourite(product)
            }
            binding.imProduct.setOnClickListener {
                iOnProduct.onImage(product)
            }
        }
    }

    fun setIOnProduct(iOnProduct: IOnFavourite){
        this.iOnProduct = iOnProduct
    }
    fun setData(listUpdate: List<Product>, context: Context) {
        this.context = context
        listProduct.clear()
        listProduct.addAll(listUpdate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemFavouriteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding, context,iOnProduct)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(listProduct[position])
    }

    override fun getItemCount(): Int {
        return listProduct.size
    }
}
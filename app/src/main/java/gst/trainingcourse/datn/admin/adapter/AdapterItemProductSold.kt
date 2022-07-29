package gst.trainingcourse.datn.admin.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import gst.trainingcourse.datn.R
import gst.trainingcourse.datn.databinding.ItemProductHistoryBinding
import gst.trainingcourse.datn.databinding.ItemProductSoldBinding
import gst.trainingcourse.datn.model.ItemHistory
import gst.trainingcourse.datn.model.ItemProductSold
import gst.trainingcourse.datn.ui.adapter.AdapterProductHistory
import java.text.DecimalFormat

class AdapterItemProductSold : RecyclerView.Adapter<AdapterItemProductSold.ItemViewHolder>() {
    private val listProduct = arrayListOf<ItemProductSold>()
    private lateinit var context: Context
    class ItemViewHolder(
        private val binding: ItemProductSoldBinding,
        private val context: Context
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(product: ItemProductSold) {
            binding.imAvatar.setImageResource(R.drawable.ic_launcher_background)
            Glide.with(context).load(product.image).into(binding.imAvatar)
            binding.tvNameProduct.text = product.name_product
            binding.tvDate.text = product.date
            binding.tvIdOrder.text = product.id_order.toString()
            binding.tvUsername.text = product.username
        }
    }
    fun setData(listUpdate: List<ItemProductSold>, context: Context) {

        this.context = context
        listProduct.clear()
        listProduct.addAll(listUpdate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemProductSoldBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding, context)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(listProduct[position])
    }

    override fun getItemCount(): Int {
        return listProduct.size
    }
}
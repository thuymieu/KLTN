package gst.trainingcourse.datn.admin.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import gst.trainingcourse.datn.R
import gst.trainingcourse.datn.databinding.ItemProductHistoryBinding
import gst.trainingcourse.datn.model.ItemHistory
import gst.trainingcourse.datn.model.Product
import java.text.DecimalFormat

class AdapterManageItemOrder : RecyclerView.Adapter<AdapterManageItemOrder.ItemViewHolder>() {
    private val listProduct = arrayListOf<ItemHistory>()
    private lateinit var context: Context
    class ItemViewHolder(
        private val binding: ItemProductHistoryBinding,
        private val context: Context
    ) : RecyclerView.ViewHolder(binding.root) {
        private val pattern = "#,##0 VNĐ"
        private val decimalFormat = DecimalFormat(pattern)
        fun bind(product: ItemHistory) {
            binding.imAvatar.setImageResource(R.drawable.ic_launcher_background)
            Glide.with(context).load(product.image).into(binding.imAvatar)
            binding.tvNameProduct.text = product.name
            binding.tvDate.text = product.date
            binding.tvPrice.text = decimalFormat.format(product.price?.toInt())
            binding.tvStatus.text = product.status
        }
    }
    fun setData(listUpdate: List<ItemHistory>, context: Context) {

        this.context = context
        listProduct.clear()
        listProduct.addAll(listUpdate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemProductHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding, context)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(listProduct[position])
    }

    override fun getItemCount(): Int {
        return listProduct.size
    }
}
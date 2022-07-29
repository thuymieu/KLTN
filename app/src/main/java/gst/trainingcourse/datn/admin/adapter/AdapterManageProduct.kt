package gst.trainingcourse.datn.admin.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import gst.trainingcourse.datn.R
import gst.trainingcourse.datn.databinding.ItemManageProductBinding
import gst.trainingcourse.datn.databinding.ItemProductBinding
import gst.trainingcourse.datn.model.Product
import gst.trainingcourse.datn.ui.adapter.AdapterProduct
import gst.trainingcourse.datn.ui.content.IOnProduct
import gst.trainingcourse.datn.utils.ItemProductDiffUtils
import java.text.DecimalFormat
import java.util.*

class AdapterManageProduct: RecyclerView.Adapter<AdapterManageProduct.ItemViewHolder>() {
    private val listProduct = arrayListOf<Product>()
    private lateinit var listProductOld : ArrayList<Product>
    private lateinit var context : Context
    private lateinit var iOnProduct: IOnClickDetail

    class ItemViewHolder(private val binding: ItemManageProductBinding, private val context: Context
                         , private val iOnProduct: IOnClickDetail
    ): RecyclerView.ViewHolder(binding.root) {
        private val pattern ="#,##0 VNĐ"
        private val decimalFormat = DecimalFormat(pattern)
        fun bind(product: Product) {
            binding.imProduct.setImageResource(R.drawable.ic_launcher_background)
            Glide.with(context).load(product.photo).into(binding.imProduct)
            binding.tvName.text = product.name
            binding.tvPrice.text = decimalFormat.format(product.price?.toInt())
            binding.btnDetail.setOnClickListener {
                iOnProduct.onClickDetail(product)
            }
            binding.btnDelete.setOnClickListener {
                iOnProduct.onClickDelete(product)
            }
        }
    }
    fun setIOnProduct(iOnProduct: IOnClickDetail){
        this.iOnProduct = iOnProduct
    }
    fun setData(listUpdate: List<Product>, context: Context) {

        this.context = context
        listProductOld = ArrayList<Product>()
        listProductOld.addAll(listUpdate)
        try {
            val diffResult = DiffUtil.calculateDiff(ItemProductDiffUtils(listProduct, listUpdate))
            diffResult.dispatchUpdatesTo(this)
            listProduct.clear()
            listProduct.addAll(listUpdate)
        } catch(e: Exception) {
            listProduct.clear()
            listProduct.addAll(listUpdate)
        }
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemManageProductBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding,context,iOnProduct)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(listProduct[position])
    }

    override fun getItemCount(): Int {
        return listProduct.size
    }
    fun getMyFilter() : Filter {
        return myFilter
    }
    private var myFilter =object: Filter() {

        override fun performFiltering(charSequence: CharSequence): FilterResults {
            val filteredList: MutableList<Product> = ArrayList()
            if (charSequence.isEmpty()) {
                filteredList.addAll(listProductOld)
            } else {
                for (ct in listProductOld) {
                    if (ct.name?.lowercase(Locale.getDefault())!!.contains(charSequence.toString()
                            .lowercase(Locale.getDefault()))) {
                        filteredList.add(ct)
                    }
                    if (ct.brand?.lowercase(Locale.getDefault())!!.contains(charSequence.toString()
                            .lowercase(Locale.getDefault()))) {
                        filteredList.add(ct)
                    }
                }
            }
            val filterResults = FilterResults()
            filterResults.values = filteredList
            return filterResults
        }

        override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
            listProduct.clear()
            listProduct.addAll(filterResults.values as Collection<Product>)
            iOnProduct.listSize(listProduct.size)
            notifyDataSetChanged()
        }
    }
}
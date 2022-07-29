package gst.trainingcourse.datn.admin.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import androidx.recyclerview.widget.RecyclerView
import gst.trainingcourse.datn.databinding.ItemManageCategoryBinding
import gst.trainingcourse.datn.databinding.ItemManageOrderBinding
import gst.trainingcourse.datn.model.Order
import gst.trainingcourse.datn.model.Product
import gst.trainingcourse.datn.model.User
import java.util.*
import kotlin.collections.ArrayList

class AdapterManageOrder : RecyclerView.Adapter<AdapterManageOrder.ItemViewHolder>() {
    private val listOrder = arrayListOf<Order>()
    private val listUser = arrayListOf<User>()
    private lateinit var listOrderOld: ArrayList<Order>
    private lateinit var context: Context
    private lateinit var iOnOrder: IOnClickDetail

    class ItemViewHolder(
        private val binding: ItemManageOrderBinding,
        private val context: Context,
        private val iOnProduct: IOnClickDetail
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(order: Order, listUser: List<User>) {
            binding.tvId.text = "Mã đơn hàng: ${order.id}"
            for (i in listUser) {
                if (i.id == order.user_id) {
                    binding.tvUsername.text = "Khách hàng: ${i.username}"
                }
            }
            binding.tvAddress.text = "Địa chỉ: ${order.address_user} "
            binding.tvPhone.text = "SĐT: ${order.phone_user} "
            binding.tvDate.text = "Ngày đặt: ${order.create_at}"
            binding.btnDetail.setOnClickListener {
                iOnProduct.onClickDetail(order)
            }
            binding.btnDelete.setOnClickListener {
                iOnProduct.onClickDelete(order)
            }
        }
    }

    fun setIOnOrder(iOnOrder: IOnClickDetail) {
        this.iOnOrder = iOnOrder
    }

    fun setData(listUpdate: List<Order>, listUser: List<User>, context: Context) {

        this.context = context
        this.listUser.addAll(listUser)
        listOrderOld = ArrayList()
        listOrderOld.addAll(listUpdate)
        listOrder.clear()
        listOrder.addAll(listUpdate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding =
            ItemManageOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding, context, iOnOrder)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(listOrder[position], listUser)
    }

    override fun getItemCount(): Int {
        return listOrder.size
    }

    fun getMyFilter(): Filter {
        return myFilter
    }

    private var myFilter = object : Filter() {
        override fun performFiltering(charSequence: CharSequence): FilterResults {
            val filteredList: MutableList<Order> = ArrayList()
            if (charSequence.isEmpty()) {
                filteredList.addAll(listOrderOld)
            } else {
                for (ct in listOrderOld) {
                    if (ct.id?.toString()?.lowercase(Locale.getDefault())!!.contains(
                            charSequence.toString()
                                .lowercase(Locale.getDefault())
                        )
                    ) {
                        filteredList.add(ct)
                    }
                }
            }
            val filterResults = FilterResults()
            filterResults.values = filteredList
            return filterResults
        }

        override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
            listOrder.clear()
            listOrder.addAll(filterResults.values as Collection<Order>)
            notifyDataSetChanged()
        }
    }
}
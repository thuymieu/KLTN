package gst.trainingcourse.datn.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import androidx.recyclerview.widget.RecyclerView
import gst.trainingcourse.datn.admin.adapter.AdapterManageOrder
import gst.trainingcourse.datn.admin.adapter.IOnClickDetail
import gst.trainingcourse.datn.databinding.ItemManageOrderBinding
import gst.trainingcourse.datn.databinding.ItemOrderBinding
import gst.trainingcourse.datn.model.Order
import gst.trainingcourse.datn.model.User
import gst.trainingcourse.datn.ui.personal.IOnOrder
import java.util.*
import kotlin.collections.ArrayList

class AdapterOrder : RecyclerView.Adapter<AdapterOrder.ItemViewHolder>() {
    private val listOrder = arrayListOf<Order>()
    private val listUser = arrayListOf<User>()
    private lateinit var listOrderOld: ArrayList<Order>
    private lateinit var context: Context
    private lateinit var iOnOrder: IOnOrder

    class ItemViewHolder(
        private val binding: ItemOrderBinding,
        private val context: Context,
        private val iOnProduct: IOnOrder
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
            binding.tvStatus.text = "Tình trạng: ${order.status}"
            binding.btnDetail.setOnClickListener {
                iOnProduct.onClickDetail(order)
            }
        }
    }

    fun setIOnOrder(iOnOrder: IOnOrder) {
        this.iOnOrder = iOnOrder
    }

    fun setData(listUpdate: List<Order>, context: Context) {

        this.context = context
        this.listUser.addAll(listUser)
        listOrderOld = ArrayList()
        listOrderOld.addAll(listUpdate)
        listOrder.clear()
        listOrder.addAll(listUpdate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding =
            ItemOrderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding, context, iOnOrder)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(listOrder[position], listUser)
    }

    override fun getItemCount(): Int {
        return listOrder.size
    }

}
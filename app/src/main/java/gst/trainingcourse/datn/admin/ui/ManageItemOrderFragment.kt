package gst.trainingcourse.datn.admin.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import gst.trainingcourse.datn.admin.adapter.AdapterManageItemOrder
import gst.trainingcourse.datn.base.BaseFragment
import gst.trainingcourse.datn.databinding.FragmentManageItemOrderBinding
import gst.trainingcourse.datn.model.ItemHistory
import gst.trainingcourse.datn.model.ItemOrder
import gst.trainingcourse.datn.model.Order
import gst.trainingcourse.datn.model.Product
import gst.trainingcourse.datn.viewmodel.MainViewModel
import java.text.DecimalFormat

class ManageItemOrderFragment : BaseFragment<FragmentManageItemOrderBinding>(){
    private var order = Order()
    private var listItemOrder = ArrayList<ItemOrder>()
    private var listHistory = ArrayList<ItemHistory>()
    private var listProduct = ArrayList<Product>()
    private val adapterItem = AdapterManageItemOrder()
    private val mainViewModel : MainViewModel by viewModels()
    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentManageItemOrderBinding {
        return FragmentManageItemOrderBinding.inflate(inflater,container,false)
    }

    override fun initData() {
        super.initData()
        val bundle = arguments
        order = bundle?.getSerializable("order") as Order
        getDataFromDatabase()
    }

    private fun getDataFromDatabase() {
        mainViewModel.getListProductAllFromRealtimeDatabase()
        mainViewModel.listProductAll.observe(viewLifecycleOwner){
            listProduct.addAll(it)
        }
        mainViewModel.getItemOrderFromRealtimeDatabase()
        mainViewModel.listItemOrder.observe(viewLifecycleOwner){
            listItemOrder.addAll(it)
            getListItemOrder(it)
        }
    }

    private fun getListItemOrder(list: ArrayList<ItemOrder>) {
        for (i in list){
            if (order.id == i.order_id){
                for(z in listProduct){
                    if (z.id == i.product_id){
                        val item = ItemHistory()
                        item.image = z.photo
                        item.name = z.name
                        item.price = z.price
                        item.date = order.create_at
                        item.status = order.status
                        listHistory.add(item)
                    }
                }
            }
        }
        setDataProduct(listHistory)
    }
    private fun setDataProduct(list: ArrayList<ItemHistory>) {
        var sum = 0
        val pattern = "#,##0 VNĐ"
        val decimalFormat = DecimalFormat(pattern)
        binding?.tvNull?.text = "Đơn đặt hàng có ${listHistory.size} sản phẩm!"
        for (i in list){
            sum += i.price!!.toInt()
        }
        binding?.tvSum?.text = "Giá trị đơn hàng: " + decimalFormat.format(sum)
        context?.let { adapterItem.setData(list, it) }
        binding?.rcListProduct?.adapter = adapterItem
        binding?.rcListProduct?.layoutManager =
            GridLayoutManager(requireContext(), 1, RecyclerView.VERTICAL, false)
    }
}
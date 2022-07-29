package gst.trainingcourse.datn.ui.personal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import gst.trainingcourse.datn.Delegate
import gst.trainingcourse.datn.base.BaseFragment
import gst.trainingcourse.datn.databinding.FragmentHistoryBinding
import gst.trainingcourse.datn.model.ItemHistory
import gst.trainingcourse.datn.model.ItemOrder
import gst.trainingcourse.datn.model.Order
import gst.trainingcourse.datn.model.Product
import gst.trainingcourse.datn.ui.adapter.AdapterProduct
import gst.trainingcourse.datn.ui.adapter.AdapterProductHistory
import gst.trainingcourse.datn.viewmodel.MainViewModel

class HistoryFragment : BaseFragment<FragmentHistoryBinding>(){
    private val adapterItem = AdapterProductHistory()
    private val mainViewModel: MainViewModel by viewModels()
    private val listOrder = ArrayList<Order>()
    private val listProduct = ArrayList<Product>()
    private val listHistory = ArrayList<ItemHistory>()
    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentHistoryBinding {
        return FragmentHistoryBinding.inflate(inflater,container,false)
    }

    override fun initData() {
        super.initData()

        getDataFromDatabase()

    }
    private fun getDataFromDatabase() {
        mainViewModel.getListProductAllFromRealtimeDatabase()
        mainViewModel.listProductAll.observe(viewLifecycleOwner){
            listProduct.addAll(it)
        }
        mainViewModel.getOrderFromRealtimeDatabase()
        mainViewModel.listOrder.observe(viewLifecycleOwner){
            getListOrderOfUser(it)
        }
        mainViewModel.getItemOrderFromRealtimeDatabase()
        mainViewModel.listItemOrder.observe(viewLifecycleOwner){
            getListItemOrderOfUser(it)
        }
    }

    private fun getListItemOrderOfUser(list: ArrayList<ItemOrder>) {
        for (i in listOrder){
            for(j in list){
                if(i.id == j.order_id){
                    for(z in listProduct){
                        if (z.id == j.product_id){
                            val item = ItemHistory()
                            item.image = z.photo
                            item.name = z.name
                            item.price = z.price
                            item.date = i.create_at
                            listHistory.add(item)
                        }
                    }
                }
            }
        }
        if(listHistory.size == 0){
            binding?.tvNull?.text = "Bạn chưa đặt hàng sản phẩm nào!"
        }
        else{
            binding?.tvNull?.text = "Bạn đã đặt hàng ${listHistory.size} sản phẩm!"
        }
        setDataProduct(listHistory)
    }

    private fun getListOrderOfUser(list: ArrayList<Order>) {
        for (i in list){
            if(i.user_id == Delegate.mainActivity.user.id){
                listOrder.add(i)
            }
        }
    }
    private fun setDataProduct(list: ArrayList<ItemHistory>) {
        context?.let { adapterItem.setData(list, it) }
        binding?.rcListProduct?.adapter = adapterItem
        binding?.rcListProduct?.layoutManager =
            GridLayoutManager(requireContext(), 1, RecyclerView.VERTICAL, false)
    }
}
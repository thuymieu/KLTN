package gst.trainingcourse.datn.ui.personal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import gst.trainingcourse.datn.Delegate
import gst.trainingcourse.datn.R
import gst.trainingcourse.datn.base.BaseFragment
import gst.trainingcourse.datn.databinding.FragmentOrderBinding
import gst.trainingcourse.datn.model.ItemHistory
import gst.trainingcourse.datn.model.ItemOrder
import gst.trainingcourse.datn.model.Order
import gst.trainingcourse.datn.model.Product
import gst.trainingcourse.datn.ui.HomeFragmentDirections
import gst.trainingcourse.datn.ui.adapter.AdapterOrder
import gst.trainingcourse.datn.viewmodel.MainViewModel

class OrderFragment : BaseFragment<FragmentOrderBinding>() {
    private val adapterItem = AdapterOrder()
    private val mainViewModel: MainViewModel by viewModels()
    private val listOrder = ArrayList<Order>()
    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentOrderBinding {
        return FragmentOrderBinding.inflate(inflater,container,false)
    }

    override fun initData() {
        super.initData()

        getDataFromDatabase()

    }

    override fun initAction() {
        super.initAction()

        adapterItem.setIOnOrder(object : IOnOrder{
            override fun onClickDetail(p: Any) {
                val order = p as Order
                val itemOrderFragment = ItemOrderFragment()
                val args = Bundle()
                args.putSerializable("order",order)
                itemOrderFragment.arguments = args
//                requireActivity().supportFragmentManager.beginTransaction().replace(
//                    R.id.fmContainer,
//                    itemOrderFragment,
//                    ItemOrderFragment::class.java.simpleName
//                )
//                    .commit()
                val action =
                    HomeFragmentDirections.actionHomeFragmentToItemOrderFragment(order)
                findNavController().navigate(action)
            }

        })
    }
    private fun getDataFromDatabase() {

        mainViewModel.getOrderFromRealtimeDatabase()
        mainViewModel.listOrder.observe(viewLifecycleOwner){
            getListOrderOfUser(it)
        }

    }

    private fun getListOrderOfUser(list: ArrayList<Order>) {
        for (i in list){
            if(i.user_id == Delegate.mainActivity.user.id){
                listOrder.add(i)
            }
        }
        if(listOrder.size == 0){
            binding?.tvNull?.text = "Bạn chưa đặt hàng sản phẩm nào!"
        }
        else{
            binding?.tvNull?.text = "Bạn đã đặt hàng ${listOrder.size} đơn hàng!"
        }
        setDataOrder(listOrder)
    }
    private fun setDataOrder(list: ArrayList<Order>) {
        context?.let { adapterItem.setData(list, it) }
        binding?.rcListOrder?.adapter = adapterItem
        binding?.rcListOrder?.layoutManager =
            GridLayoutManager(requireContext(), 1, RecyclerView.VERTICAL, false)
    }
}
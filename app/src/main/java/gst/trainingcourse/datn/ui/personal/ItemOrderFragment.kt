package gst.trainingcourse.datn.ui.personal

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase
import gst.trainingcourse.datn.Delegate
import gst.trainingcourse.datn.base.BaseFragment
import gst.trainingcourse.datn.databinding.FragmentItemOrderBinding
import gst.trainingcourse.datn.model.ItemHistory
import gst.trainingcourse.datn.model.ItemOrder
import gst.trainingcourse.datn.model.Order
import gst.trainingcourse.datn.model.Product
import gst.trainingcourse.datn.ui.HomeFragmentDirections
import gst.trainingcourse.datn.ui.adapter.AdapterProductHistory
import gst.trainingcourse.datn.viewmodel.MainViewModel

class ItemOrderFragment : BaseFragment<FragmentItemOrderBinding>() {
    private var order = Order()
    private val adapterItem = AdapterProductHistory()
    private val mainViewModel: MainViewModel by viewModels()
    private var listItemOrder = ArrayList<ItemOrder>()
    private val listProduct = ArrayList<Product>()
    private val listHistory = ArrayList<ItemHistory>()
    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentItemOrderBinding {
        return FragmentItemOrderBinding.inflate(inflater, container, false)
    }

    override fun initData() {
        super.initData()

        val bundle = arguments
        order = bundle?.getSerializable("order") as Order
        if (order.status.toString() != "Chưa xác nhận") {
            binding?.btnDeleteOrder?.isVisible = false
        }
        getDataFromDatabase()
    }

    override fun initAction() {
        super.initAction()

        binding?.btnDeleteOrder?.setOnClickListener {

            AlertDialog.Builder(Delegate.mainActivity)
                .setTitle("Thông báo!")
                .setMessage("Bạn muốn hủy đơn hàng này! ")
                .setPositiveButton(
                    "Xác nhận"
                ) { _, _ ->
                    changeStatusProduct(order)
                }
                .setNegativeButton("Hủy", null)
                .show()
        }
    }

    private fun getDataFromDatabase() {
        mainViewModel.getListProductAllFromRealtimeDatabase()
        mainViewModel.listProductAll.observe(viewLifecycleOwner) {
            listProduct.addAll(it)
        }
        mainViewModel.getItemOrderFromRealtimeDatabase()
        mainViewModel.listItemOrder.observe(viewLifecycleOwner) {
            listItemOrder.addAll(it)
        }
        mainViewModel.getItemOrderFromRealtimeDatabase()
        mainViewModel.listItemOrder.observe(viewLifecycleOwner) {
            getListItemOrderOfUser(it)
        }
    }

    private fun getListItemOrderOfUser(list: ArrayList<ItemOrder>) {
        for (j in list) {
            if (order.id == j.order_id) {
                for (z in listProduct) {
                    if (z.id == j.product_id) {
                        val item = ItemHistory()
                        item.id = z.id
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
        binding?.tvAmount?.text = "Đơn hàng có ${listHistory.size} sản phẩm!"
        setDataProduct(listHistory)
    }

    private fun setDataProduct(list: ArrayList<ItemHistory>) {
        context?.let { adapterItem.setData(list, it) }
        binding?.rcListProduct?.adapter = adapterItem
        binding?.rcListProduct?.layoutManager =
            GridLayoutManager(requireContext(), 1, RecyclerView.VERTICAL, false)
    }

    private fun changeStatusProduct(order: Order) {
        for (i in listHistory) {
            for (j in listProduct) {
                if (j.id == i.id) {
                    j.status = "Yes"
                    val myRef = FirebaseDatabase.getInstance().getReference("Products")
                    myRef.child(j.id.toString()).setValue(j)
                }
            }
        }
        order.status = "Đã hủy"
        val myRef = FirebaseDatabase.getInstance().getReference("Order")
        myRef.child(order.id.toString()).setValue(order) { _, _ ->
            Toast.makeText(
                context,
                "Hủy thành công!",
                Toast.LENGTH_LONG
            ).show()
        }
        val action =
            ItemOrderFragmentDirections.actionItemOrderFragmentToHomeFragment()
        findNavController().navigate(action)
    }
}
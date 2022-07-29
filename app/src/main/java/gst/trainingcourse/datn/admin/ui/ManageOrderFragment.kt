package gst.trainingcourse.datn.admin.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase
import gst.trainingcourse.datn.Delegate
import gst.trainingcourse.datn.R
import gst.trainingcourse.datn.admin.adapter.AdapterManageOrder
import gst.trainingcourse.datn.admin.adapter.IOnClickDetail
import gst.trainingcourse.datn.base.BaseFragment
import gst.trainingcourse.datn.databinding.FragmentManageOrderBinding
import gst.trainingcourse.datn.model.ItemOrder
import gst.trainingcourse.datn.model.Order
import gst.trainingcourse.datn.model.Product
import gst.trainingcourse.datn.model.User
import gst.trainingcourse.datn.ui.HomeFragmentDirections
import gst.trainingcourse.datn.ui.login.LoginFragmentDirections
import gst.trainingcourse.datn.viewmodel.MainViewModel

class ManageOrderFragment:BaseFragment<FragmentManageOrderBinding>() {
    private val mainViewModel: MainViewModel by viewModels()
    private val adapterOrder = AdapterManageOrder()
    private val listUser = ArrayList<User>()
    private var listItemOrder = ArrayList<ItemOrder>()
    private val listProduct = ArrayList<Product>()
    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentManageOrderBinding {
        return FragmentManageOrderBinding.inflate(inflater,container,false)
    }

    override fun initData() {
        super.initData()
        mainViewModel.getUserFromRealtimeDatabase()
        mainViewModel.user.observe(viewLifecycleOwner) {
            listUser.addAll(it)
        }
        mainViewModel.getListProductAllFromRealtimeDatabase()
        mainViewModel.listProductAll.observe(viewLifecycleOwner){
            listProduct.addAll(it)
        }
        mainViewModel.getItemOrderFromRealtimeDatabase()
        mainViewModel.listItemOrder.observe(viewLifecycleOwner){
            listItemOrder.addAll(it)
        }
        mainViewModel.getOrderFromRealtimeDatabase()
        mainViewModel.listOrder.observe(viewLifecycleOwner) {
            setDataCategory(it)
        }
    }
    override fun initAction() {
        super.initAction()
        adapterOrder.setIOnOrder(object : IOnClickDetail {
            override fun onClickDetail(p: Any) {
                val order = p as Order
                val bundle = Bundle()
                bundle.putSerializable("order",order)
                findNavController().navigate(R.id.action_manageOrderFragment_to_manageItemOrderFragment,bundle)
            }

            override fun onClickDelete(p: Any) {
                val order = p as Order
                AlertDialog.Builder(Delegate.adminActivity)
                    .setTitle("Thông báo!")
                    .setMessage("Bạn muốn xóa ${order.id} khỏi đơn đặt hàng! ")
                    .setPositiveButton(
                        "Xóa"
                    ) { _, _ ->
                        changeStatusProduct(order)
                    }
                    .setNegativeButton("Hủy", null)
                    .show()
            }

            override fun listSize(i: Int) {

            }
        })

        binding?.search?.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapterOrder.getMyFilter().filter(newText)
                return false
            }

        })
    }

    private fun changeStatusProduct(order: Order) {
        val idOrder = order.id
        for(i in listItemOrder){
            if (i.order_id == idOrder){
                for(j in listProduct){
                    if(j.id == i.product_id){
                        val p = j
                        j.status = "Yes"
                        val myRef = FirebaseDatabase.getInstance().getReference("Products")
                        myRef.child(p.id.toString()).setValue(p)
                    }
                }
                val myRef = FirebaseDatabase.getInstance().getReference("ItemOrder")
                myRef.child(i.id.toString()).removeValue()
            }
        }
        val myRef = FirebaseDatabase.getInstance().getReference("Order")
        myRef.child(idOrder.toString()).removeValue { _, _ ->
            Toast.makeText(
                context,
                "Xóa thành công!",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun setDataCategory(listOrder: ArrayList<Order>) {
        binding?.tvAmount?.text = "Có ${listOrder.size} đơn hàng"
        context?.let { adapterOrder.setData(listOrder,listUser, it) }
        binding?.rcOrder?.adapter = adapterOrder
        binding?.rcOrder?.layoutManager =
            GridLayoutManager(requireContext(), 1, RecyclerView.VERTICAL, false)
    }
}
package gst.trainingcourse.datn.admin.ui

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase
import gst.trainingcourse.datn.R
import gst.trainingcourse.datn.admin.adapter.AdapterManageOrder
import gst.trainingcourse.datn.admin.adapter.IOnClickDetail
import gst.trainingcourse.datn.base.BaseFragment
import gst.trainingcourse.datn.databinding.FragmentManageOrderBinding
import gst.trainingcourse.datn.model.*
import gst.trainingcourse.datn.viewmodel.MainViewModel
import kotlin.collections.ArrayList

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

            //Update
            override fun onClickDelete(p: Any) {
                val order = p as Order
                when (order.status) {
                    "???? h???y" -> {
                        Toast.makeText(context,"????n h??ng ???? ???????c h???y",Toast.LENGTH_LONG).show()
                    }
                    "???? giao" -> {
                        Toast.makeText(context,"????n h??ng ???? giao th??nh c??ng",Toast.LENGTH_LONG).show()
                    }
                    else -> {
                        openDialogUpdate(Gravity.CENTER,order)
                    }
                }

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
                    if(j.id == i.product_id) {
                        j.status = "Yes"
                        val myRef = FirebaseDatabase.getInstance().getReference("Products")
                        myRef.child(j.id.toString()).setValue(j)
                    }
                }
            }
        }
        order.status = "???? h???y"
        val myRef = FirebaseDatabase.getInstance().getReference("Order")
        myRef.child(idOrder.toString()).setValue(order) { _, _ ->
            Toast.makeText(
                context,
                "X??a th??nh c??ng!",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun openDialogUpdate(gravity: Int, order: Order) {
        val dialog = context?.let { Dialog(it) }
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.setContentView(R.layout.layout_update_status_order)
        val window = dialog?.window ?: return

        window.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        window.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val windowAttribute = window.attributes
        windowAttribute.gravity = gravity
        window.attributes = windowAttribute

        if (Gravity.BOTTOM == gravity) {
            dialog.setCancelable(true)
        } else {
            dialog.setCancelable(false)
        }

        val btnCancel = dialog.findViewById<Button>(R.id.btnCancelUpdateStatusOrder)
        val btnUpdateStatus = dialog.findViewById<Button>(R.id.btnUpdateStatusOrder)
        val btnDelete = dialog.findViewById<Button>(R.id.btnDeleteOrder)
        val tvOldStatus = dialog.findViewById<TextView>(R.id.tvOldStatus)
        val tvNewStatus = dialog.findViewById<TextView>(R.id.tvNewStatus)

        tvOldStatus.text = order.status
        when(order.status){
            "Ch??a x??c nh???n" -> {
                tvNewStatus.text = "???? x??c nh???n"
                btnDelete.visibility = View.VISIBLE
            }
            "???? x??c nh???n" -> {
                tvNewStatus.text = "??ang giao h??ng"
            }
            "??ang giao h??ng" -> {
                tvNewStatus.text = "???? giao"
            }
        }
        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        btnDelete.setOnClickListener {
            changeStatusProduct(order)
        }

        btnUpdateStatus.setOnClickListener {
            order.status = tvNewStatus.text.toString()
            val myRef = FirebaseDatabase.getInstance().getReference("Order")
            myRef.child(order.id.toString()).setValue(order) { _, _ ->
                Toast.makeText(
                    context,
                    "C???p nh???t th??nh c??ng!",
                    Toast.LENGTH_LONG
                ).show()
            }
            dialog.dismiss()
        }
        dialog.show()
    }
    private fun setDataCategory(listOrder: ArrayList<Order>) {
        binding?.tvAmount?.text = "C?? ${listOrder.size} ????n h??ng"
        context?.let { adapterOrder.setData(listOrder,listUser, it) }
        binding?.rcOrder?.adapter = adapterOrder
        binding?.rcOrder?.layoutManager =
            GridLayoutManager(requireContext(), 1, RecyclerView.VERTICAL, false)
    }
}
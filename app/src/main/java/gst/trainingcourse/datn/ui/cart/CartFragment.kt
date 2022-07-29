package gst.trainingcourse.datn.ui.cart

import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase
import gst.trainingcourse.datn.Delegate
import gst.trainingcourse.datn.R
import gst.trainingcourse.datn.base.BaseFragment
import gst.trainingcourse.datn.databinding.FragmentCartBinding
import gst.trainingcourse.datn.databinding.FragmentContentBinding
import gst.trainingcourse.datn.model.ItemOrder
import gst.trainingcourse.datn.model.Order
import gst.trainingcourse.datn.model.Product
import gst.trainingcourse.datn.ui.adapter.AdapterProduct
import gst.trainingcourse.datn.ui.adapter.AdapterProductInCart
import gst.trainingcourse.datn.ui.personal.PersonalFragment
import gst.trainingcourse.datn.viewmodel.MainViewModel
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class CartFragment : BaseFragment<FragmentCartBinding>() {
    private val adapterProduct = AdapterProductInCart()
    private val pattern = "#,##0 VNĐ"
    private val decimalFormat = DecimalFormat(pattern)
    private val mainViewModel: MainViewModel by viewModels()
    private val listOrder = ArrayList<Order>()
    private val listItemOrder = ArrayList<ItemOrder>()
    private val listProduct = ArrayList<Product>()
    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentCartBinding {
        return FragmentCartBinding.inflate(inflater, container, false)
    }

    override fun initData() {
        super.initData()

        setData(Delegate.mainActivity.listCart)
        getDataFromDatabase()
        adapterProduct.setIOnProduct(object : IOnDeleteProduct {
            override fun onImage(p: Product) {
                AlertDialog.Builder(Delegate.mainActivity)
                    .setTitle("Thông báo!")
                    .setMessage("Bạn muốn xóa ${p.name} khỏi giỏ hàng! ")
                    .setPositiveButton(
                        "Xóa"
                    ) { _, _ ->
                        Toast.makeText(context, "Xóa sản phẩm thành công!", Toast.LENGTH_LONG)
                            .show()
                        Delegate.mainActivity.listCart.remove(p)
                        setData(Delegate.mainActivity.listCart)
                    }
                    .setNegativeButton("Hủy", null)
                    .show()
                }
        })
    }

    override fun initAction() {
        super.initAction()
        binding?.btnThanhToan?.setOnClickListener {
            if (Delegate.mainActivity.user.username == null){
                Toast.makeText(context,"Bạn cần đăng nhập để thanh toán",Toast.LENGTH_LONG).show()
            }
            else{
                confirmInfor(Gravity.CENTER)
            }
        }
    }

    private fun confirmInfor(gravity: Int) {
        val dialog = context?.let { Dialog(it) }
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.setContentView(R.layout.layout_infor_payment)
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

        val btnCancel = dialog.findViewById<Button>(R.id.btnCancel)
        val btnConfirm = dialog.findViewById<Button>(R.id.btnPayment)
        val edAddress = dialog.findViewById<EditText>(R.id.edAddress)
        val edPhone = dialog.findViewById<EditText>(R.id.edPhone)

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        btnConfirm.setOnClickListener {
            if (edAddress.text.toString() == "" || edPhone.text.toString() == "") {
                Toast.makeText(context, "Nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show()
            } else {
                payment(edAddress.text.toString().trim(),edPhone.text.toString().trim())
                dialog.dismiss()
            }
        }
        dialog.show()
    }

    private fun payment(edAddress :String,edPhone:String){
        val order = Order()
        if (listOrder.size == 0){
            order.id = 1
        }
        else{
            order.id = listOrder[listOrder.size -1].id!!.toInt() + 1
        }
        val sdf = SimpleDateFormat("dd/M/yyyy")
        val currentDate = sdf.format(Date())
        order.status = "Yes"
        order.phone_user = edPhone
        order.address_user = edAddress
        order.user_id = Delegate.mainActivity.user.id
        order.create_at = currentDate.toString()
        addOrderToDatabase(order)

        val itemOrder = ItemOrder()
        var count = 1
        for(i in listProduct){
            if (listItemOrder.size == 0){
                itemOrder.id = count
            }
            else{
                itemOrder.id = listItemOrder[listItemOrder.size -1].id!!.toInt() + count
            }
            count++
            itemOrder.order_id = order.id
            itemOrder.product_id =i.id
            itemOrder.quantity = 1
            addItemOrderToDatabase(itemOrder)

            changeStatusProduct(i)
        }
        Delegate.mainActivity.listCart.clear()
        requireActivity().supportFragmentManager.beginTransaction().replace(
            R.id.fmContainer,
            CartFragment(),
            CartFragment::class.java.simpleName).commit()
    }

    private fun changeStatusProduct(product:Product) {
        val myRef = FirebaseDatabase.getInstance().getReference("Products")
        product.status = "No"
        myRef.child(product.id.toString()).setValue(product)
    }

    private fun getDataFromDatabase() {
        mainViewModel.getOrderFromRealtimeDatabase()
        mainViewModel.listOrder.observe(viewLifecycleOwner){
            listOrder.addAll(it)
        }
        mainViewModel.getItemOrderFromRealtimeDatabase()
        mainViewModel.listItemOrder.observe(viewLifecycleOwner){
            listItemOrder.addAll(it)
        }
    }

    private fun addItemOrderToDatabase(itemOrder: ItemOrder) {
        val myRef = FirebaseDatabase.getInstance().getReference("ItemOrder")
        myRef.child(itemOrder.id.toString()).setValue(itemOrder)
    }

    private fun addOrderToDatabase(order: Order) {
        val myRef = FirebaseDatabase.getInstance().getReference("Order")
        myRef.child(order.id.toString()).setValue(order) { _, _ ->
            Toast.makeText(
                Delegate.mainActivity,
                "Thanh toán thành công!",
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun setData(listProducts: ArrayList<Product>) {
        var tong = 0
        listProduct.addAll(listProducts)
        context?.let { adapterProduct.setData(listProducts, it) }
        binding?.rcViewCart?.adapter = adapterProduct
        binding?.rcViewCart?.layoutManager = GridLayoutManager(
            requireContext(), 1,
            RecyclerView.VERTICAL, false
        )
        for (i in Delegate.mainActivity.listCart) {
            tong += i.price!!.toInt()
        }
        binding?.tvTong?.text = decimalFormat.format(tong)
        if (listProducts.size > 0) {
            binding?.llPayment?.isVisible = true
            binding?.btnThanhToan?.isVisible = true
            binding?.tvNull?.isGone = true
        } else {
            binding?.llPayment?.isGone = true
            binding?.btnThanhToan?.isGone = true
            binding?.tvNull?.isVisible = true
        }
    }
}
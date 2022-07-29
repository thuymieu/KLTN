package gst.trainingcourse.datn.admin.ui

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase
import gst.trainingcourse.datn.Delegate
import gst.trainingcourse.datn.R
import gst.trainingcourse.datn.admin.adapter.AdapterCategorySelect
import gst.trainingcourse.datn.admin.adapter.AdapterManageProduct
import gst.trainingcourse.datn.admin.adapter.IOnClickDetail
import gst.trainingcourse.datn.base.BaseFragment
import gst.trainingcourse.datn.databinding.FragmentManageProductBinding
import gst.trainingcourse.datn.model.Category
import gst.trainingcourse.datn.model.ItemOrder
import gst.trainingcourse.datn.model.Product
import gst.trainingcourse.datn.viewmodel.MainViewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ManageProductFragment:BaseFragment<FragmentManageProductBinding>() {
    private lateinit var adapterCategorySelect : AdapterCategorySelect
    private val adapterProduct = AdapterManageProduct()
    private val listProduct = ArrayList<Product>()
    private val listCategory = ArrayList<Category>()
    private val mainViewModel: MainViewModel by viewModels()
    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentManageProductBinding {
        return FragmentManageProductBinding.inflate(inflater,container,false)
    }

    override fun initData() {
        super.initData()
        mainViewModel.getListCategoryFromRealtimeDatabase()
        mainViewModel.listCategory.observe(viewLifecycleOwner) {
            listCategory.addAll(it)
        }
        mainViewModel.getListProductFromRealtimeDatabase()
        mainViewModel.listProduct.observe(viewLifecycleOwner) {
            setDataProduct(it)
            listProduct.addAll(it)
        }
    }

    override fun initAction() {
        super.initAction()
        adapterProduct.setIOnProduct(object : IOnClickDetail {

            override fun onClickDetail(p: Any) {
                openDialogUpdate(Gravity.CENTER,p as Product)
            }

            override fun onClickDelete(p: Any) {
                val product = p as Product
                deleteToDatabase(product)
            }

            override fun listSize(i: Int) {
                binding?.tvAmount?.text = "Có ${i} sản phẩm!"
            }
        })

        binding?.btnAdd?.setOnClickListener {
            openDialogAdd(Gravity.CENTER)
        }

        binding?.search?.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapterProduct.getMyFilter().filter(newText)
                return false
            }
        })
    }
    private fun setDataProduct(listProducts: ArrayList<Product>) {
        binding?.tvAmount?.text = "Có ${listProducts.size} sản phẩm!"
        context?.let { adapterProduct.setData(listProducts, it) }
        binding?.rcProduct?.adapter = adapterProduct
        binding?.rcProduct?.layoutManager =
            GridLayoutManager(requireContext(), 1, RecyclerView.VERTICAL, false)
    }

    private fun openDialogUpdate(gravity: Int, p: Product) {
        val dialog = context?.let { Dialog(it) }
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.setContentView(R.layout.layout_update_product)
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

        var positionCate = 0
        for (i in listCategory){
            if(i.id == p.id_category){
                positionCate = listCategory.indexOf(i)
            }
        }
        val spnCategory = dialog.findViewById<Spinner>(R.id.spnCategory)
        adapterCategorySelect =
            context?.let { AdapterCategorySelect(it,R.layout.item_manage_select_categorry,listCategory) }!!
        spnCategory.adapter = adapterCategorySelect
        spnCategory.setSelection(positionCate)
        val btnCancel = dialog.findViewById<Button>(R.id.btnCancelUpdateProduct)
        val btnUpdate = dialog.findViewById<Button>(R.id.btnUpdateProduct)
        val edName = dialog.findViewById<EditText>(R.id.edNameUpdateProduct)
        val edBrand = dialog.findViewById<EditText>(R.id.edBrandUpdateProduct)
        val edPrice = dialog.findViewById<EditText>(R.id.edPriceUpdateProduct)
        val edImage = dialog.findViewById<EditText>(R.id.edImageUpdateProduct)
        val edDetail = dialog.findViewById<EditText>(R.id.edDetailUpdateProduct)
        val edColor = dialog.findViewById<EditText>(R.id.edColorUpdateProduct)
        val edYearManufacture= dialog.findViewById<EditText>(R.id.edYearManufactureUpdateProduct)
        val edYearUse = dialog.findViewById<EditText>(R.id.edYearUseUpdateProduct)
        val edOdo = dialog.findViewById<EditText>(R.id.edOdoUpdateProduct)


        edName.setText(p.name)
        edBrand.setText(p.brand)
        edPrice.setText(p.price?.toInt().toString())
        edImage.setText(p.photo)
        edDetail.setText(p.detail)
        edColor.setText(p.color)
        edYearManufacture.setText(p.year_manufacture.toString())
        edYearUse.setText(p.number_years_use.toString())
        edOdo.setText(p.odo.toString())

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        btnUpdate.setOnClickListener {
            if (edName.text.toString() == "" || edImage.text.toString() == ""
                || edBrand.text.toString() == ""
                || edPrice.text.toString() == "" || edDetail.text.toString() == ""
                || edColor.text.toString() == "" || edYearManufacture.text.toString() == ""
                || edYearUse.text.toString() == "" || edOdo.text.toString() == "") {
                Toast.makeText(context, "Nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show()
            } else {
                val item = spnCategory.selectedItem as Category
                p.id_category = item.id
                p.name = edName.text.toString().trim()
                p.brand = edBrand.text.toString().trim()
                p.price = edPrice.text.toString().toDouble()
                p.photo = edImage.text.toString().trim()
                p.detail = edDetail.text.toString().trim()
                p.odo = edOdo.text.toString().toInt()
                p.year_manufacture = edYearManufacture.text.toString().toInt()
                p.number_years_use = edYearUse.text.toString().toInt()
                p.color = edColor.text.toString().trim()
                addToDatabase(p,"Cập nhật thành công!")
                dialog.dismiss()
            }
        }
        dialog.show()
    }

    private fun openDialogAdd(gravity: Int) {
        val dialog = context?.let { Dialog(it) }
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.setContentView(R.layout.layout_add_product)
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

        val spnCategory = dialog.findViewById<Spinner>(R.id.spnCategory)
        adapterCategorySelect =
            context?.let { AdapterCategorySelect(it,R.layout.item_manage_select_categorry,listCategory) }!!
        spnCategory.adapter = adapterCategorySelect
        val btnCancel = dialog.findViewById<Button>(R.id.btnCancelAddProduct)
        val btnAdd = dialog.findViewById<Button>(R.id.btnAddProduct)
        val edName = dialog.findViewById<EditText>(R.id.edNameAddProduct)
        val edBrand = dialog.findViewById<EditText>(R.id.edBrandAddProduct)
        val edPrice = dialog.findViewById<EditText>(R.id.edPriceAddProduct)
        val edImage = dialog.findViewById<EditText>(R.id.edImageAddProduct)
        val edDetail = dialog.findViewById<EditText>(R.id.edDetailAddProduct)
        val edColor = dialog.findViewById<EditText>(R.id.edColorAddProduct)
        val edYearManufacture= dialog.findViewById<EditText>(R.id.edYearManufactureAddProduct)
        val edYearUse = dialog.findViewById<EditText>(R.id.edYearUseAddProduct)
        val edOdo = dialog.findViewById<EditText>(R.id.edOdoAddProduct)

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        btnAdd.setOnClickListener {
            if (edName.text.toString() == "" || edImage.text.toString() == ""
                || edBrand.text.toString() == ""
                || edPrice.text.toString() == "" || edDetail.text.toString() == ""
                || edColor.text.toString() == "" || edYearManufacture.text.toString() == ""
                || edYearUse.text.toString() == "" || edOdo.text.toString() == "") {
                Toast.makeText(context, "Nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show()
            } else {
                val productNew = Product()
                if (listProduct.size == 0) {
                    productNew.id = 1
                } else {
                    productNew.id = listProduct[listProduct.size - 1].id!!.toInt() + 1
                }
                val sdf = SimpleDateFormat("dd/M/yyyy")
                val currentDate = sdf.format(Date())
                val item = spnCategory.selectedItem as Category
                productNew.id_category = item.id
                productNew.name = edName.text.toString().trim()
                productNew.brand = edBrand.text.toString().trim()
                productNew.price = edPrice.text.toString().toDouble()
                productNew.photo = edImage.text.toString().trim()
                productNew.detail = edDetail.text.toString().trim()
                productNew.discount = 0
                productNew.status = "Yes"
                productNew.created_at = currentDate.toString()
                productNew.odo = edOdo.text.toString().toInt()
                productNew.year_manufacture = edYearManufacture.text.toString().toInt()
                productNew.number_years_use = edYearUse.text.toString().toInt()
                productNew.color = edColor.text.toString().trim()
                addToDatabase(productNew,"Thêm thành công!")
                dialog.dismiss()
            }
        }
        dialog.show()
    }

    private fun addToDatabase(product: Product, s :String) {
        val myRef = FirebaseDatabase.getInstance().getReference("Products")
        myRef.child(product.id.toString()).setValue(product) { _, _ ->
            Toast.makeText(
                context,
                s,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun deleteToDatabase(p: Product) {
        AlertDialog.Builder(Delegate.adminActivity)
            .setTitle("Thông báo!")
            .setMessage("Bạn muốn xóa ${p.name} khỏi danh sách! ")
            .setPositiveButton(
                "Xóa"
            ) { _, _ ->
                if(p.status == "No"){
                    Toast.makeText(
                        context,
                        "Sản phẩm đang có trong đơn đặt hàng, không thể xóa",
                        Toast.LENGTH_LONG
                    ).show()
                }else{
                    val myRef = FirebaseDatabase.getInstance().getReference("Products")
                    myRef.child(p.id.toString()).removeValue { _, _ ->
                        Toast.makeText(
                            context,
                            "Xóa thành công!",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
            }
            .setNegativeButton("Hủy", null)
            .show()
    }
}
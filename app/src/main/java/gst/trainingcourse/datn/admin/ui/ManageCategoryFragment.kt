package gst.trainingcourse.datn.admin.ui

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase
import gst.trainingcourse.datn.Delegate
import gst.trainingcourse.datn.R
import gst.trainingcourse.datn.admin.adapter.AdapterManageCategory
import gst.trainingcourse.datn.admin.adapter.IOnClickDetail
import gst.trainingcourse.datn.base.BaseFragment
import gst.trainingcourse.datn.databinding.FragmentManageCategoryBinding
import gst.trainingcourse.datn.model.Category
import gst.trainingcourse.datn.model.News
import gst.trainingcourse.datn.model.Product
import gst.trainingcourse.datn.viewmodel.MainViewModel

class ManageCategoryFragment : BaseFragment<FragmentManageCategoryBinding>() {
    private val mainViewModel: MainViewModel by viewModels()
    private val adapterCategory = AdapterManageCategory()
    private val listCategory = ArrayList<Category>()
    private val listProduct = ArrayList<Product>()
    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentManageCategoryBinding {
        return FragmentManageCategoryBinding.inflate(inflater, container, false)
    }

    override fun initData() {
        super.initData()
        mainViewModel.getListProductAllFromRealtimeDatabase()
        mainViewModel.listProductAll.observe(viewLifecycleOwner){
            listProduct.addAll(it)
        }
        mainViewModel.getListCategoryFromRealtimeDatabase()
        mainViewModel.listCategory.observe(viewLifecycleOwner) {
            setDataCategory(it)
            listCategory.addAll(it)
            binding?.tvAmount?.text = "Có ${listCategory.size} danh mục"
        }
    }

    override fun initAction() {
        super.initAction()
        adapterCategory.setIOnCategory(object : IOnClickDetail {
            override fun onClickDetail(p: Any) {
                openDialogUpdate(Gravity.CENTER,p as Category)
            }

            override fun onClickDelete(p: Any) {
                val cate = p as Category
                deleteToDatabase(cate)
            }

            override fun listSize(i: Int) {

            }

        })
        binding?.btnAdd?.setOnClickListener {
            openDialogAdd(Gravity.CENTER)
        }
    }

    private fun openDialogUpdate(gravity: Int, cate: Category) {
        val dialog = context?.let { Dialog(it) }
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.setContentView(R.layout.layout_update_category)
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

        val btnCancel = dialog.findViewById<Button>(R.id.btnCancelUpdateCategory)
        val btnUpdate = dialog.findViewById<Button>(R.id.btnUpdateCategory)
        val edName = dialog.findViewById<EditText>(R.id.edNameUpdateCategory)
        val edImage = dialog.findViewById<EditText>(R.id.edImageUpdateCategory)

        edName.setText(cate.category_name)
        edImage.setText(cate.photo)
        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        btnUpdate.setOnClickListener {
            if (edName.text.toString() == "" || edImage.text.toString() == "") {
                Toast.makeText(context, "Nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show()
            } else {
                cate.category_name = edName.text.toString().trim()
                cate.photo = edImage.text.toString().trim()
                addCateToDatabase(cate,"Cập nhật thành công!")
                dialog.dismiss()
            }
        }
        dialog.show()
    }

    private fun openDialogAdd(gravity: Int) {
        val dialog = context?.let { Dialog(it) }
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.setContentView(R.layout.layout_add_category)
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

        val btnCancel = dialog.findViewById<Button>(R.id.btnCancelCategory)
        val btnAdd = dialog.findViewById<Button>(R.id.btnAddCategory)
        val edName = dialog.findViewById<EditText>(R.id.edNameCategory)
        val edImage = dialog.findViewById<EditText>(R.id.edImageCategory)

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        btnAdd.setOnClickListener {
            if (edName.text.toString() == "" || edImage.text.toString() == "") {
                Toast.makeText(context, "Nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show()
            } else {
                val cate = Category()
                if (listCategory.size == 0) {
                    cate.id = 1
                } else {
                    cate.id = listCategory[listCategory.size - 1].id!!.toInt() + 1
                }
                cate.category_name = edName.text.toString().trim()
                cate.photo = edImage.text.toString().trim()
                addCateToDatabase(cate,"Thêm thành công!")
                dialog.dismiss()
            }
        }
        dialog.show()
    }

    private fun addCateToDatabase(cate: Category,s :String) {
        val myRef = FirebaseDatabase.getInstance().getReference("Brand")
        myRef.child(cate.id.toString()).setValue(cate){
            _,_ ->
            Toast.makeText(
                context,
                s,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun deleteToDatabase(cate: Category) {
        AlertDialog.Builder(Delegate.adminActivity)
            .setTitle("Thông báo!")
            .setMessage("Bạn muốn xóa ${cate.category_name} khỏi danh sách! ")
            .setPositiveButton(
                "Xóa"
            ) { _, _ ->
                var check = true
                for(i in listProduct){
                    if (cate.id == i.id_category){
                        check = false
                    }
                }
                if (check){
                    val myRef = FirebaseDatabase.getInstance().getReference("Brand")
                    myRef.child(cate.id.toString()).removeValue { _, _ ->
                        Toast.makeText(
                            context,
                            "Xóa thành công!",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
                else{
                    Toast.makeText(
                        context,
                        "Danh mục đang có sản phẩm, không thể xóa!",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
            .setNegativeButton("Hủy", null)
            .show()
    }

    private fun setDataCategory(listCategory: ArrayList<Category>) {
        context?.let { adapterCategory.setData(listCategory, it) }
        binding?.rcCategory?.adapter = adapterCategory
        binding?.rcCategory?.layoutManager =
            GridLayoutManager(requireContext(), 1, RecyclerView.VERTICAL, false)
    }
}
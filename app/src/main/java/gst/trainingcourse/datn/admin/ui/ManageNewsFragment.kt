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
import gst.trainingcourse.datn.admin.adapter.AdapterManageNews
import gst.trainingcourse.datn.admin.adapter.IOnClickDetail
import gst.trainingcourse.datn.base.BaseFragment
import gst.trainingcourse.datn.databinding.FragmentManageNewsBinding
import gst.trainingcourse.datn.model.News
import gst.trainingcourse.datn.model.Product
import gst.trainingcourse.datn.viewmodel.MainViewModel
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ManageNewsFragment : BaseFragment<FragmentManageNewsBinding>() {
    private val mainViewModel: MainViewModel by viewModels()
    private val adapterNews = AdapterManageNews()
    private val listNews = ArrayList<News>()
    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentManageNewsBinding {
        return FragmentManageNewsBinding.inflate(inflater, container, false)
    }

    override fun initData() {
        super.initData()
        mainViewModel.getNewsFromRealtimeDatabase()
        mainViewModel.listNews.observe(viewLifecycleOwner) {
            setDataNews(it)
            listNews.addAll(it)
        }
    }

    override fun initAction() {
        super.initAction()
        adapterNews.setIOnCategory(object : IOnClickDetail {
            override fun onClickDetail(p: Any) {
                openDialogUpdate(Gravity.CENTER, p as News)
            }

            override fun onClickDelete(p: Any) {
                val news = p as News
                deleteToDatabase(news)
            }

            override fun listSize(i: Int) {

            }
        })
        binding?.btnAdd?.setOnClickListener {
            openDialogAdd(Gravity.CENTER)
        }
    }

    private fun openDialogUpdate(gravity: Int, news: News) {
        val dialog = context?.let { Dialog(it) }
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.setContentView(R.layout.layout_update_news)
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

        val btnCancel = dialog.findViewById<Button>(R.id.btnCancelUpdateNews)
        val btnUpdate = dialog.findViewById<Button>(R.id.btnUpdateNews)
        val edTitle = dialog.findViewById<EditText>(R.id.edTitleUpdateNews)
        val edUri = dialog.findViewById<EditText>(R.id.edUriUpdateNews)

        edTitle.setText(news.title)
        edUri.setText(news.content)
        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        btnUpdate.setOnClickListener {
            if (edTitle.text.toString() == "" || edUri.text.toString() == "") {
                Toast.makeText(context, "Nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show()
            } else {
                val sdf = SimpleDateFormat("dd/M/yyyy")
                val currentDate = sdf.format(Date())
                news.title = edTitle.text.toString().trim()
                news.content = edUri.text.toString().trim()
                news.created_at = currentDate.toString()
                addNewsToDatabase(news,"Cập nhật thành công!")
                dialog.dismiss()
            }
        }
        dialog.show()
    }

    private fun openDialogAdd(gravity: Int) {
        val dialog = context?.let { Dialog(it) }
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.setContentView(R.layout.layout_add_news)
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

        val btnCancel = dialog.findViewById<Button>(R.id.btnCancelAddNews)
        val btnAdd = dialog.findViewById<Button>(R.id.btnAddNews)
        val edTitle = dialog.findViewById<EditText>(R.id.edTitleAddNews)
        val edUri = dialog.findViewById<EditText>(R.id.edUriAddNews)

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        btnAdd.setOnClickListener {
            if (edTitle.text.toString() == "" || edUri.text.toString() == "") {
                Toast.makeText(context, "Nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show()
            } else {
                val sdf = SimpleDateFormat("dd/M/yyyy")
                val currentDate = sdf.format(Date())
                val news = News()
                if (listNews.size == 0) {
                    news.id = 1
                } else {
                    news.id = listNews[listNews.size - 1].id!!.toInt() + 1
                }
                news.title = edTitle.text.toString().trim()
                news.content = edUri.text.toString().trim()
                news.created_at = currentDate.toString()
                addNewsToDatabase(news,"Thêm thành công!")
                dialog.dismiss()
            }
        }
        dialog.show()
    }

    private fun addNewsToDatabase(news: News,s : String) {
        val myRef = FirebaseDatabase.getInstance().getReference("News")
        myRef.child(news.id.toString()).setValue(news){_,_->
            Toast.makeText(
                context,
                s,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    private fun deleteToDatabase(news: News) {
        AlertDialog.Builder(Delegate.adminActivity)
            .setTitle("Thông báo!")
            .setMessage("Bạn muốn xóa ${news.title} khỏi danh sách! ")
            .setPositiveButton(
                "Xóa"
            ) { _, _ ->
                val myRef = FirebaseDatabase.getInstance().getReference("News")
                myRef.child(news.id.toString()).removeValue { _, _ ->
                    Toast.makeText(
                        context,
                        "Xóa thành công!",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
            .setNegativeButton("Hủy", null)
            .show()
    }

    private fun setDataNews(list: ArrayList<News>) {
        binding?.tvAmount?.text = "Có ${list.size} tin tức"
        context?.let { adapterNews.setData(list, it) }
        binding?.rcNews?.adapter = adapterNews
        binding?.rcNews?.layoutManager =
            GridLayoutManager(requireContext(), 1, RecyclerView.VERTICAL, false)
    }
}
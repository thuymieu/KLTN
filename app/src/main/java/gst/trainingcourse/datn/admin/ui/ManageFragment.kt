package gst.trainingcourse.datn.admin.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import gst.trainingcourse.datn.R
import gst.trainingcourse.datn.admin.adapter.AdapterCategorySelect
import gst.trainingcourse.datn.base.BaseFragment
import gst.trainingcourse.datn.databinding.FragmentManageBinding
import gst.trainingcourse.datn.model.Category
import java.util.*
import kotlin.collections.ArrayList

class ManageFragment:BaseFragment<FragmentManageBinding>() {
    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentManageBinding {
        return FragmentManageBinding.inflate(inflater,container,false)
    }

    override fun initAction() {
        super.initAction()

        binding?.quanLyDanhMuc?.setOnClickListener {
            findNavController().navigate(R.id.action_manageFragment_to_manageCategoryFragment)
        }
        binding?.quanLySanPham?.setOnClickListener {
            findNavController().navigate(R.id.action_manageFragment_to_manageProductFragment)
        }
        binding?.quanLyDonDatHang?.setOnClickListener {
            findNavController().navigate(R.id.action_manageFragment_to_manageOrderFragment)
        }
        binding?.quanLyTinTuc?.setOnClickListener {
            findNavController().navigate(R.id.action_manageFragment_to_manageNewsFragment)
        }
        binding?.thongKe?.setOnClickListener {
            findNavController().navigate(R.id.action_manageFragment_to_statisticalFragment)
        }
        binding?.quanLyNguoiDung?.setOnClickListener {
            findNavController().navigate(R.id.action_manageFragment_to_manageUserFragment)
        }
    }
}
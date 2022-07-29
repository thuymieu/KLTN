package gst.trainingcourse.datn.admin.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.FirebaseDatabase
import gst.trainingcourse.datn.Delegate
import gst.trainingcourse.datn.admin.adapter.AdapterManageUser
import gst.trainingcourse.datn.admin.adapter.IOnClickDetail
import gst.trainingcourse.datn.base.BaseFragment
import gst.trainingcourse.datn.databinding.FragmentManageUseBinding
import gst.trainingcourse.datn.model.Order
import gst.trainingcourse.datn.model.User
import gst.trainingcourse.datn.viewmodel.MainViewModel

class ManageUserFragment : BaseFragment<FragmentManageUseBinding>() {
    private val mainViewModel : MainViewModel by viewModels()
    private val adapterUser = AdapterManageUser()
    private val listUser = ArrayList<User>()
    private val listOrder = ArrayList<Order>()
    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentManageUseBinding {
        return FragmentManageUseBinding.inflate(inflater,container,false)
    }

    override fun initData() {
        super.initData()
        mainViewModel.getOrderFromRealtimeDatabase()
        mainViewModel.listOrder.observe(viewLifecycleOwner){
            listOrder.addAll(it)
        }
        mainViewModel.getUserFromRealtimeDatabase()
        mainViewModel.user.observe(viewLifecycleOwner){
            setDataUser(it)
            listUser.addAll(it)
        }
    }

    override fun initAction() {
        super.initAction()
        adapterUser.setIOnCategory(object : IOnClickDetail{
            override fun onClickDetail(p: Any) {
                val user = p as User
                user.password = "111111"
                resetPassword(user)
            }

            override fun onClickDelete(p: Any) {
                val user = p as User
                deleteUser(user)
            }

            override fun listSize(i: Int) {

            }

        })

        binding?.searchUser?.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapterUser.getMyFilter().filter(newText)
                return false
            }
        })
    }

    private fun resetPassword(user: User) {
        AlertDialog.Builder(Delegate.adminActivity)
            .setTitle("Thông báo!")
            .setMessage("Bạn muốn đặt lại mật khẩu cho ${user.username}! ")
            .setPositiveButton(
                "Xác nhận"
            ) { _, _ ->
                val myRef = FirebaseDatabase.getInstance().getReference("User")
                myRef.child(user.id.toString()).setValue(user) { _, _ ->
                    Toast.makeText(
                        context,
                        "Cập nhật thành công!",
                        Toast.LENGTH_LONG
                    ).show()
                }
            }
            .setNegativeButton("Hủy", null)
            .show()
    }

    private fun deleteUser(user: User) {
        AlertDialog.Builder(Delegate.adminActivity)
            .setTitle("Thông báo!")
            .setMessage("Bạn muốn xóa ${user.username} khỏi danh sách! ")
            .setPositiveButton(
                "Xóa"
            ) { _, _ ->
                var check = true
                for(i in listOrder){
                    if (i.user_id == user.id){
                        check = false
                        Toast.makeText(
                            context,
                            "Người dùng này đang có đơn hàng đặt",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
                if(check){
                    val myRef = FirebaseDatabase.getInstance().getReference("User")
                    myRef.child(user.id.toString()).removeValue { _, _ ->
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

    private fun setDataUser(list: ArrayList<User>) {
        binding?.tvAmount?.text = "Có ${list.size} người dùng"
        context?.let { adapterUser.setData(list, it) }
        binding?.rcUser?.adapter = adapterUser
        binding?.rcUser?.layoutManager =
            GridLayoutManager(requireContext(), 1, RecyclerView.VERTICAL, false)
    }
}
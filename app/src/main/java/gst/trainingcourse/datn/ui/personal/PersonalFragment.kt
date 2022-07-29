package gst.trainingcourse.datn.ui.personal

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.*
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.navigation.fragment.findNavController
import androidx.viewbinding.ViewBindings
import com.google.firebase.database.FirebaseDatabase
import gst.trainingcourse.datn.Delegate
import gst.trainingcourse.datn.R
import gst.trainingcourse.datn.base.BaseFragment
import gst.trainingcourse.datn.databinding.FragmentPersonalBinding
import gst.trainingcourse.datn.model.User

class PersonalFragment : BaseFragment<FragmentPersonalBinding>() {
    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentPersonalBinding {
        return FragmentPersonalBinding.inflate(inflater, container, false)
    }

    override fun initView() {
        super.initView()
        binding?.tvHello?.text = "Xin chào ${Delegate.mainActivity.user.username} !"
    }

    override fun initAction() {
        super.initAction()
        binding?.tvHistory?.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction().replace(
                R.id.fmContainer,
                HistoryFragment(),
                HistoryFragment::class.java.simpleName
            ).commit()
        }
        binding?.tvLogout?.setOnClickListener {
            Delegate.mainActivity.user = User()
            findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
        }
        binding?.tvUpdateUser?.setOnClickListener {
            openDialogUpdate(Gravity.CENTER)
        }
    }

    private fun openDialogUpdate(gravity: Int) {
        val dialog = context?.let { Dialog(it) }
        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.setContentView(R.layout.layout_dialog_update_user)
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
        val btnConfirm = dialog.findViewById<Button>(R.id.btnConfirm)
        val edPassword = dialog.findViewById<EditText>(R.id.edPassword)
        val edConfirmPassword = dialog.findViewById<EditText>(R.id.edConfirmPassword)

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        btnConfirm.setOnClickListener {
            if (edConfirmPassword.text.toString() == "" || edPassword.text.toString() == "") {
                Toast.makeText(context, "Nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show()
            } else {
                when {
                    edPassword.text.length < 6 -> {
                        Toast.makeText(context, "Độ dài quá ngắn", Toast.LENGTH_SHORT).show()
                    }
                    edConfirmPassword.text.toString() != edPassword.text.toString() -> {
                        Toast.makeText(context, "Xác nhận không hợp lệ", Toast.LENGTH_SHORT).show()
                    }
                    else -> {
                        changePassword(edPassword.text.toString())
                        dialog.dismiss()
                    }
                }
            }
        }
        dialog.show()
    }

    private fun changePassword(password : String) {
        val user = Delegate.mainActivity.user
        user.password = password
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("User")
        val pathObject = user.id.toString()
        myRef.child(pathObject).setValue(user
        ) { _, _ -> Toast.makeText(context, "Thay đổi mật khẩu thành công!", Toast.LENGTH_LONG).show() }
    }
}
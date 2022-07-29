package gst.trainingcourse.datn.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import gst.trainingcourse.datn.Delegate
import gst.trainingcourse.datn.R
import gst.trainingcourse.datn.base.BaseFragment
import gst.trainingcourse.datn.databinding.FragmentSingUpBinding
import gst.trainingcourse.datn.model.User
import gst.trainingcourse.datn.viewmodel.MainViewModel

class SingUpFragment:BaseFragment<FragmentSingUpBinding>() {
    private val mainViewModel: MainViewModel by viewModels()
    private var listUser = ArrayList<User>()
    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentSingUpBinding {
        return FragmentSingUpBinding.inflate(inflater,container,false)
    }

    override fun initView() {
        super.initView()
        animateView()
        binding?.btnOfPanelLogin?.setOnClickListener {
            findNavController().navigate(R.id.action_singUpFragment_to_loginFragment)
        }
    }

    override fun initData() {
        super.initData()
        mainViewModel.getUserFromRealtimeDatabase()
        mainViewModel.user.observe(viewLifecycleOwner){
            listUser = it
        }
        binding?.btnSignup?.setOnClickListener {
            if (binding?.edUsername?.text.toString()=="" ||binding?.edPassword?.text.toString() == ""
                ||binding?.edConfirmPassword?.text.toString()==""||binding?.edPhoneNumber?.text.toString()=="")
            {
                Toast.makeText(context,"Nhập đầy đủ các thông tin", Toast.LENGTH_LONG).show()
            }
            else{
                var checkSignUp = false
                for (i in listUser){
                    if (i.username == binding?.edUsername?.text.toString()){
                        checkSignUp =true
                        Toast.makeText(context,"Tên tài khoản tồn tại", Toast.LENGTH_LONG).show()
                    }
                }
                if(!checkSignUp){
                    when {
                        binding?.edPassword?.text?.length!! < 6 -> {
                            Toast.makeText(context,"Mật khẩu quá ngắn!", Toast.LENGTH_LONG).show()
                        }
                        binding?.edConfirmPassword?.text.toString() != binding?.edPassword?.text.toString() -> {
                            Toast.makeText(context,"Xác nhận mật khẩu không đúng!", Toast.LENGTH_LONG).show()
                        }
                        else -> {
                            val user = User()
                            user.id = listUser.size +1
                            user.username = binding?.edUsername?.text.toString().trim()
                            user.password = binding?.edPassword?.text.toString().trim()
                            user.phone = binding?.edPhoneNumber?.text.toString().trim()
                            addUserToRealtimeDatabase(user)
                        }
                    }
                }
            }
        }
    }

    private fun addUserToRealtimeDatabase(user: User) {
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("User")
        val pathObject = user.id.toString()
        myRef.child(pathObject).setValue(user
        ) { _, _ -> Toast.makeText(context, "Đăng ký thành công!", Toast.LENGTH_LONG).show() }
    }

    private fun animateView(){
        binding?.viewUsername?.translationX = 800f
        binding?.viewPassword?.translationX = 800f
        binding?.viewConfirmPassword?.translationX = 800f
        binding?.viewPhoneNumber?.translationX = 800f
        binding?.btnSignup?.translationX = 800f

        binding?.viewUsername?.alpha = 0f
        binding?.viewPassword?.alpha = 0f
        binding?.viewConfirmPassword?.alpha = 0f
        binding?.viewPhoneNumber?.alpha = 0f
        binding?.btnSignup?.alpha = 0f

        binding?.viewUsername?.animate()?.translationX(0f)?.alpha(1f)?.setDuration(800)?.setStartDelay(200)?.start();
        binding?.viewPassword?.animate()?.translationX(0f)?.alpha(1f)?.setDuration(800)?.setStartDelay(400)?.start();
        binding?.viewConfirmPassword?.animate()?.translationX(0f)?.alpha(1f)?.setDuration(800)?.setStartDelay(600)?.start();
        binding?.viewPhoneNumber?.animate()?.translationX(0f)?.alpha(1f)?.setDuration(800)?.setStartDelay(800)?.start();
        binding?.btnSignup?.animate()?.translationX(0f)?.alpha(1f)?.setDuration(800)?.setStartDelay(1000)?.start();

    }
}
package gst.trainingcourse.datn.ui.login

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import gst.trainingcourse.datn.Delegate
import gst.trainingcourse.datn.R
import gst.trainingcourse.datn.base.BaseFragment
import gst.trainingcourse.datn.databinding.FragmentLoginBinding
import gst.trainingcourse.datn.model.User
import gst.trainingcourse.datn.viewmodel.MainViewModel

class LoginFragment:BaseFragment<FragmentLoginBinding>() {
    private val mainViewModel: MainViewModel by viewModels()
    private var listUser = ArrayList<User>()
    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentLoginBinding {
        return FragmentLoginBinding.inflate(inflater,container,false)
    }

    override fun initView() {
        super.initView()
        animateView()
        binding?.btnOfPanelSignup?.setOnClickListener {
            findNavController().navigate(R.id.action_loginFragment_to_singUpFragment)
        }
    }

    override fun initData() {
        super.initData()
        mainViewModel.getUserFromRealtimeDatabase()
        mainViewModel.user.observe(viewLifecycleOwner){
            listUser = it
        }
        binding?.btnLogin?.setOnClickListener {
            if (binding?.edUsername?.text.toString()=="Admin" && binding?.edPassword?.text.toString() == "1"){
                Delegate.mainActivity.intentToAdmin()
                return@setOnClickListener
            }
            if (binding?.edUsername?.text.toString()=="" ||binding?.edPassword?.text.toString() == "")
            {
                Toast.makeText(context,"Nhập đầy đủ các thông tin",Toast.LENGTH_LONG).show()
            }
            else{
                var checkLogin = false
                for (i in listUser){
                    if (i.username == binding?.edUsername?.text.toString() && i.password.toString() == binding?.edPassword?.text.toString()){
                        checkLogin =true
                        Delegate.mainActivity.setUser(i)
                        Toast.makeText(context,"Đăng nhập thành công!",Toast.LENGTH_LONG).show()
                        findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                    }
                }
                if(!checkLogin){
                    Toast.makeText(context,"Tài khoản không đúng",Toast.LENGTH_LONG).show()
                }
            }
        }

    }
    private fun animateView(){
        binding?.tvFillTheForm?.translationX = 800f
        binding?.viewUsername?.translationX = 800f
        binding?.viewPassword?.translationX = 800f
        binding?.tvForgotPassword?.translationX = 800f
        binding?.btnLogin?.translationX = 800f

        binding?.tvFillTheForm?.alpha = 0f
        binding?.viewUsername?.alpha = 0f
        binding?.viewPassword?.alpha = 0f
        binding?.tvForgotPassword?.alpha = 0f
        binding?.btnLogin?.alpha = 0f

        binding?.tvFillTheForm?.animate()?.translationX(0f)?.alpha(1f)?.setDuration(800)?.setStartDelay(200)?.start()
        binding?.viewUsername?.animate()?.translationX(0f)?.alpha(1f)?.setDuration(800)?.setStartDelay(400)?.start()
        binding?.viewPassword?.animate()?.translationX(0f)?.alpha(1f)?.setDuration(800)?.setStartDelay(600)?.start()
        binding?.tvForgotPassword?.animate()?.translationX(0f)?.alpha(1f)?.setDuration(800)?.setStartDelay(800)?.start()
        binding?.btnLogin?.animate()?.translationX(0f)?.alpha(1f)?.setDuration(800)?.setStartDelay(1000)?.start()
    }
}
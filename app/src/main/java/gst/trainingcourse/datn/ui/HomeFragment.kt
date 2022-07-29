package gst.trainingcourse.datn.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.navigation.NavigationBarView
import gst.trainingcourse.datn.Delegate
import gst.trainingcourse.datn.R
import gst.trainingcourse.datn.base.BaseFragment
import gst.trainingcourse.datn.databinding.FragmentHomeBinding
import gst.trainingcourse.datn.ui.cart.CartFragment
import gst.trainingcourse.datn.ui.content.ContentFragment
import gst.trainingcourse.datn.ui.favorite.FavoriteFragment
import gst.trainingcourse.datn.ui.personal.PersonalFragment

class HomeFragment :BaseFragment<FragmentHomeBinding>(), NavigationBarView.OnItemSelectedListener {
    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentHomeBinding {
        return FragmentHomeBinding.inflate(inflater,container,false)
    }
    override fun initView() {
        super.initView()
        val navigationView : BottomNavigationView = binding?.bottomNavView!!
        navigationView.setOnItemSelectedListener(this)

        requireActivity().supportFragmentManager.beginTransaction().replace(R.id.fmContainer,
            ContentFragment(),
            ContentFragment::class.java.simpleName).commit()

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.itemHome ->{
                requireActivity().supportFragmentManager.beginTransaction().replace(R.id.fmContainer,
                    ContentFragment(),
                    ContentFragment::class.java.simpleName).commit()
            }
            R.id.itemFavorite -> {
                requireActivity().supportFragmentManager.beginTransaction().replace(R.id.fmContainer,
                    FavoriteFragment(),
                    FavoriteFragment::class.java.simpleName).commit()
            }
            R.id.itemCart -> {
                requireActivity().supportFragmentManager.beginTransaction().replace(R.id.fmContainer,
                    CartFragment(),
                    CartFragment::class.java.simpleName).commit()
            }
            R.id.itemUser -> {
                if(Delegate.mainActivity.user.username == null){
                    findNavController().navigate(R.id.action_homeFragment_to_loginFragment)
                }
                else{
                    requireActivity().supportFragmentManager.beginTransaction().replace(R.id.fmContainer,
                        PersonalFragment(),
                        PersonalFragment::class.java.simpleName).commit()
                }
            }
        }
        return true
    }
}
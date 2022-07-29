package gst.trainingcourse.datn.ui

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import gst.trainingcourse.datn.Delegate
import gst.trainingcourse.datn.R
import gst.trainingcourse.datn.admin.ui.AdminActivity
import gst.trainingcourse.datn.databinding.ActivityMainBinding
import gst.trainingcourse.datn.model.Product
import gst.trainingcourse.datn.model.User

class MainActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var binding: ActivityMainBinding
    var user = User()
    var listCart = ArrayList<Product>()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Delegate.mainActivity = this
        navController = (supportFragmentManager.findFragmentById(R.id.nav_host_fragment_container)
                as NavHostFragment).navController

    }
    @JvmName("setUser1")
    fun setUser(user: User){
        this.user = user
    }
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
    fun intentToAdmin(){
        val intent = Intent(this, AdminActivity::class.java)
        startActivity(intent)
    }
}
package gst.trainingcourse.datn.admin.ui

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import gst.trainingcourse.datn.Delegate
import gst.trainingcourse.datn.R
import gst.trainingcourse.datn.databinding.ActivityAdminBinding
import gst.trainingcourse.datn.databinding.ActivityMainBinding
import gst.trainingcourse.datn.model.Product
import gst.trainingcourse.datn.model.User
import gst.trainingcourse.datn.ui.MainActivity

class AdminActivity : AppCompatActivity() {
    private lateinit var navController: NavController
    private lateinit var binding: ActivityAdminBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAdminBinding.inflate(layoutInflater)
        setContentView(binding.root)
        Delegate.adminActivity = this
        navController = (supportFragmentManager.findFragmentById(R.id.nav_host_admin_container)
                as NavHostFragment).navController
    }
    override fun onSupportNavigateUp(): Boolean {
        return navController.navigateUp() || super.onSupportNavigateUp()
    }
    fun intentToHomePage(){
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }
}
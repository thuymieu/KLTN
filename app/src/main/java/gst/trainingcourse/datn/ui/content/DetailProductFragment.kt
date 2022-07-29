package gst.trainingcourse.datn.ui.content

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.google.firebase.database.FirebaseDatabase
import gst.trainingcourse.datn.Delegate
import gst.trainingcourse.datn.R
import gst.trainingcourse.datn.base.BaseFragment
import gst.trainingcourse.datn.databinding.FragmentDetailProductBinding
import gst.trainingcourse.datn.model.ItemFavourite
import gst.trainingcourse.datn.model.Product
import gst.trainingcourse.datn.model.User
import gst.trainingcourse.datn.ui.adapter.AdapterProductInWishlist
import gst.trainingcourse.datn.viewmodel.MainViewModel
import java.text.DecimalFormat

class DetailProductFragment:BaseFragment<FragmentDetailProductBinding>() {
    private val mainViewModel: MainViewModel by viewModels()
    private val listFavourite = ArrayList<ItemFavourite>()
    private val listProduct = ArrayList<Product>()
    private var product = Product()
    private var check = false
    private var checkFavourite = false
    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentDetailProductBinding {
        return FragmentDetailProductBinding.inflate(inflater,container,false)
    }

    @SuppressLint("SetTextI18n")
    override fun initData() {
        super.initData()
        val pattern ="#,##0 VNĐ"
        val decimalFormat = DecimalFormat(pattern)
        val args: DetailProductFragmentArgs by navArgs()
        product = args.product!!
        binding?.tvName?.text = product.name
        binding?.imProduct?.setImageResource(R.drawable.ic_launcher_background)
        context?.let { binding?.imProduct?.let { it1 -> Glide.with(it).load(product.photo).into(it1) } }
        binding?.tvOdo?.text = product.odo.toString() + " km"
        binding?.tvType?.text = product.brand
        binding?.tvCalendar?.text = product.number_years_use.toString() + " năm"
        binding?.tvValue?.text = decimalFormat.format(product.price?.toInt())
        binding?.tvStatus?.text = "Còn hàng"
        binding?.tvYearUse?.text = product.year_manufacture.toString()
        binding?.tvColor?.text = product.color
        binding?.tvDetail?.text = product.detail

        if(Delegate.mainActivity.user.username != null){
            Delegate.mainActivity.user.id?.let { mainViewModel.getFavouriteFromRealtimeDatabase() }
            mainViewModel.listFavourite.observe(viewLifecycleOwner) {
                listFavourite.addAll(it)
            }
            mainViewModel.getListProductAllFromRealtimeDatabase()
            mainViewModel.listProductAll.observe(viewLifecycleOwner){
                listProduct.addAll(it)
            }
        }
    }

    override fun initView() {
        super.initView()
    }

    override fun initAction() {
        super.initAction()
        binding?.rlCall?.setOnClickListener {
            if (Delegate.mainActivity.user.username == null){
                Toast.makeText(context,"Bạn cần đăng nhập để thêm vào danh sách yêu thích",Toast.LENGTH_LONG).show()
            }
            else{
                for(i in listFavourite){
                    if(i.product_id == product.id)
                    {
                        checkFavourite =true
                        Toast.makeText(context,"Sản phẩm đã có trong danh sách yêu thích",Toast.LENGTH_LONG).show()
                    }
                }
                if (!checkFavourite){
                    val itemFavourite = ItemFavourite()
                    if (listFavourite.size == 0){
                        itemFavourite.id = 1
                    }
                    else{
                        itemFavourite.id = listFavourite[listFavourite.size -1].id!!.toInt() + 1
                    }
                    itemFavourite.product_id = product.id
                    itemFavourite.user_id = Delegate.mainActivity.user.id
                    addWishlistToRealtimeDatabase(itemFavourite)
                }
            }
        }

        binding?.rlAdd?.setOnClickListener {
            for(i in Delegate.mainActivity.listCart){
                if(i.id == product.id){
                    check = true
                    Toast.makeText(context,"Sản phẩm đã có trong giỏ hàng",Toast.LENGTH_LONG).show()
                }
            }
            if(!check){
                var kt=true
                for(i in listProduct){
                    if (i.id == product.id && i.status == "No"){
                        kt = false
                        Toast.makeText(context,"Sản phẩm đã được mua!",Toast.LENGTH_SHORT).show()
                    }
                }
                if(kt){
                    Toast.makeText(context,"Thêm sản phẩm thành công!",Toast.LENGTH_LONG).show()
                    Delegate.mainActivity.listCart.add(product)
                }
            }
        }
    }
    private fun addWishlistToRealtimeDatabase(item: ItemFavourite) {
        val database = FirebaseDatabase.getInstance()
        val myRef = database.getReference("Wishlist")
        val pathObject = item.id.toString()
        myRef.child(pathObject).setValue(item
        ) { _, _ -> Toast.makeText(context, "Thêm thành công!", Toast.LENGTH_LONG).show() }
    }
}
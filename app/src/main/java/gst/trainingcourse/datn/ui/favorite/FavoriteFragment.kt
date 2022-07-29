package gst.trainingcourse.datn.ui.favorite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import gst.trainingcourse.datn.Delegate
import gst.trainingcourse.datn.base.BaseFragment
import gst.trainingcourse.datn.databinding.FragmentFavoriteBinding
import gst.trainingcourse.datn.model.ItemFavourite
import gst.trainingcourse.datn.model.Product
import gst.trainingcourse.datn.ui.HomeFragmentDirections
import gst.trainingcourse.datn.ui.adapter.AdapterProduct
import gst.trainingcourse.datn.ui.adapter.AdapterProductInWishlist
import gst.trainingcourse.datn.viewmodel.MainViewModel

class FavoriteFragment : BaseFragment<FragmentFavoriteBinding>() {
    private val mainViewModel: MainViewModel by viewModels()
    private val listFavourite = ArrayList<Product>()
    private val listProduct = ArrayList<Product>()
    private val listItemFavourite = ArrayList<ItemFavourite>()
    private val adapterProduct = AdapterProductInWishlist()
    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentFavoriteBinding {
        return FragmentFavoriteBinding.inflate(inflater, container, false)
    }

    override fun initData() {
        super.initData()
        if (Delegate.mainActivity.user.username == null) {
            binding?.tvNull?.isVisible = true
        } else {
            mainViewModel.getListProductFromRealtimeDatabase()
            mainViewModel.listProduct.observe(viewLifecycleOwner) {
                listProduct.addAll(it)
            }
            binding?.tvNull?.isGone == true
            mainViewModel.getFavouriteFromRealtimeDatabase()
            mainViewModel.listFavourite.observe(viewLifecycleOwner) {
                setData(it)
            }
        }
        adapterProduct.setIOnProduct(object : IOnFavourite {
            override fun onFavourite(p: Product) {
                AlertDialog.Builder(Delegate.mainActivity)
                    .setTitle("Thông báo!")
                    .setMessage("Bạn muốn xóa ${p.name} khỏi danh sách yêu thích! ")
                    .setPositiveButton(
                        "Xóa"
                    ) { _, _ ->
                        var idItemDelete: Int? = null
                        for (i in listItemFavourite) {
                            if (i.product_id == p.id && i.user_id == Delegate.mainActivity.user.id) {
                                idItemDelete = i.id!!
                            }
                        }
                        val myRef = FirebaseDatabase.getInstance().getReference("Wishlist")
                        myRef.child(idItemDelete!!.toString()).removeValue { _, _ ->
                            Toast.makeText(
                                context,
                                "Xóa sản phẩm thành công!",
                                Toast.LENGTH_LONG
                            ).show()

                        }

                    }
                    .setNegativeButton("Hủy", null)
                    .show()
            }

            override fun onImage(p: Product) {
                val action =
                    HomeFragmentDirections.actionHomeFragmentToDetailProductFragment(p)
                findNavController().navigate(action)
            }

        })
    }

    private fun setData(list: ArrayList<ItemFavourite>) {
        if (listFavourite != null) {
            listFavourite.clear()
        }
        listItemFavourite.addAll(list)
        for (i in listProduct) {
            for (j in list) {
                if (i.id == j.product_id && j.user_id == Delegate.mainActivity.user.id) {
                    listFavourite.add(i)
                }
            }
        }
        if (listFavourite.size == 0) {
            Toast.makeText(context, "Chưa có sản phẩm yêu thích!", Toast.LENGTH_LONG).show()
        }
        context?.let { adapterProduct.setData(listFavourite, it) }
        binding?.rcListFavourite?.adapter = adapterProduct
        binding?.rcListFavourite?.layoutManager =
            GridLayoutManager(requireContext(), 2, RecyclerView.VERTICAL, false)

    }
}
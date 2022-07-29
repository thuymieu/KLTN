package gst.trainingcourse.datn.ui.content

import android.content.Intent
import android.graphics.drawable.AnimationDrawable
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import gst.trainingcourse.datn.Delegate
import gst.trainingcourse.datn.R
import gst.trainingcourse.datn.admin.adapter.AdapterManageNews
import gst.trainingcourse.datn.base.BaseFragment
import gst.trainingcourse.datn.databinding.FragmentContentBinding
import gst.trainingcourse.datn.model.Category
import gst.trainingcourse.datn.model.News
import gst.trainingcourse.datn.model.Product
import gst.trainingcourse.datn.model.User
import gst.trainingcourse.datn.ui.HomeFragmentDirections
import gst.trainingcourse.datn.ui.adapter.AdapterCategory
import gst.trainingcourse.datn.ui.adapter.AdapterNews
import gst.trainingcourse.datn.ui.adapter.AdapterProduct
import gst.trainingcourse.datn.viewmodel.MainViewModel

class ContentFragment : BaseFragment<FragmentContentBinding>() {
    private val adapterProduct = AdapterProduct()
    private val adapterCategory = AdapterCategory()
    private val adapterNews= AdapterNews()
    private val mainViewModel: MainViewModel by viewModels()
    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentContentBinding {
        return FragmentContentBinding.inflate(inflater, container, false)
    }

    override fun initData() {
        super.initData()
        mainViewModel.getListProductFromRealtimeDatabase()
        mainViewModel.getListCategoryFromRealtimeDatabase()
        mainViewModel.listProduct.observe(viewLifecycleOwner) {
            setDataProduct(it)
        }
        mainViewModel.listCategory.observe(viewLifecycleOwner) {
            setDataCategory(it)
        }
        mainViewModel.getNewsFromRealtimeDatabase()
        mainViewModel.listNews.observe(viewLifecycleOwner) {
            setDataNews(it)
        }
    }

    override fun initView() {
        super.initView()
        val animationDrawable = binding?.lvImage?.drawable as AnimationDrawable
        animationDrawable.start();
    }

    override fun initAction() {
        super.initAction()
        adapterProduct.setIOnProduct(object : IOnProduct {
            override fun onImageProduct(product: Product) {
                val action =
                    HomeFragmentDirections.actionHomeFragmentToDetailProductFragment(product)
                findNavController().navigate(action)
            }
        })
        adapterCategory.setIOnCategory(object : IOnCategory {
            override fun onImageCategory(cate: Category) {
                val action = HomeFragmentDirections.actionHomeFragmentToListProductFragment(cate)
                findNavController().navigate(action)
            }

        })
        adapterNews.setIOnNews(object : IOnNews{
            override fun onDetail(news: News) {
                val uri = Uri.parse(news.content)
                val intent = Intent(Intent.ACTION_VIEW,uri)
                startActivity(intent)
            }
        })

        binding?.search?.setOnQueryTextListener(object : SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapterProduct.getMyFilter().filter(newText)
                return false
            }

        })
    }

    private fun setDataProduct(listProducts: ArrayList<Product>) {
        context?.let { adapterProduct.setData(listProducts, it) }
        binding?.rcContent?.adapter = adapterProduct
        binding?.rcContent?.layoutManager =
            GridLayoutManager(requireContext(), 2, RecyclerView.HORIZONTAL, false)
    }

    private fun setDataCategory(listCategory: ArrayList<Category>) {
        context?.let { adapterCategory.setData(listCategory, it) }
        binding?.rcCategory?.adapter = adapterCategory
        binding?.rcCategory?.layoutManager =
            GridLayoutManager(requireContext(), 1, RecyclerView.HORIZONTAL, false)
    }
    private fun setDataNews(listNews: ArrayList<News>) {
        context?.let { adapterNews.setData(listNews, it) }
        binding?.rcNews?.adapter = adapterNews
        binding?.rcNews?.layoutManager =
            GridLayoutManager(requireContext(), 1, RecyclerView.VERTICAL, false)
    }
}
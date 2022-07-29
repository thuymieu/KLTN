package gst.trainingcourse.datn.ui.content

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import gst.trainingcourse.datn.base.BaseFragment
import gst.trainingcourse.datn.databinding.FragmentListProductBinding
import gst.trainingcourse.datn.model.Product
import gst.trainingcourse.datn.ui.HomeFragmentDirections
import gst.trainingcourse.datn.ui.adapter.AdapterProduct
import gst.trainingcourse.datn.viewmodel.MainViewModel

class ListProductFragment:BaseFragment<FragmentListProductBinding>() {
    private val mainViewModel: MainViewModel by viewModels()
    private val adapterProduct = AdapterProduct()
    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentListProductBinding {
        return FragmentListProductBinding.inflate(inflater,container,false)
    }

    override fun initData() {
        super.initData()
        val args: ListProductFragmentArgs by navArgs()
        val cate = args.category
        mainViewModel.getListProductFromRealtimeDatabaseWithCate(cate?.id!!)
        mainViewModel.listProductWithCategory.observe(viewLifecycleOwner){
            setDataProduct(it)
        }
    }
    private fun setDataProduct(listProducts : ArrayList<Product>) {
        context?.let { adapterProduct.setData(listProducts, it) }
        binding?.rcListProduct?.adapter = adapterProduct
        binding?.rcListProduct?.layoutManager = GridLayoutManager(requireContext(),2,
            RecyclerView.VERTICAL,false)
    }

    override fun initAction() {
        super.initAction()
        adapterProduct.setIOnProduct(object : IOnProduct{
            override fun onImageProduct(product: Product) {
                val action = ListProductFragmentDirections.actionListProductFragmentToDetailProductFragment(product)
                findNavController().navigate(action)
            }
        })
    }
}
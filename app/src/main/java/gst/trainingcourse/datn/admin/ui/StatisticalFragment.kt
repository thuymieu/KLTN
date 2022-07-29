package gst.trainingcourse.datn.admin.ui

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import gst.trainingcourse.datn.admin.adapter.AdapterItemProductSold
import gst.trainingcourse.datn.base.BaseFragment
import gst.trainingcourse.datn.databinding.FragmentStatisticalBinding
import gst.trainingcourse.datn.model.*
import gst.trainingcourse.datn.viewmodel.MainViewModel
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class StatisticalFragment : BaseFragment<FragmentStatisticalBinding>() {
    private val mainViewModel: MainViewModel by viewModels()
    private val listProductAll = ArrayList<Product>()
    private val listOrder = ArrayList<Order>()
    private val listItemOrder = ArrayList<ItemOrder>()
    private val listUser = ArrayList<User>()
    private val listProductSold = ArrayList<ItemProductSold>()
    private val adapter = AdapterItemProductSold()
    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): FragmentStatisticalBinding {
        return FragmentStatisticalBinding.inflate(inflater, container, false)
    }

    override fun initData() {
        super.initData()
        mainViewModel.getListProductAllFromRealtimeDatabase()
        mainViewModel.listProductAll.observe(viewLifecycleOwner) {
            listProductAll.addAll(it)
        }

        mainViewModel.getOrderFromRealtimeDatabase()
        mainViewModel.listOrder.observe(viewLifecycleOwner) {
            listOrder.addAll(it)
        }

        mainViewModel.getUserFromRealtimeDatabase()
        mainViewModel.user.observe(viewLifecycleOwner) {
            listUser.addAll(it)
        }

        mainViewModel.getItemOrderFromRealtimeDatabase()
        mainViewModel.listItemOrder.observe(viewLifecycleOwner) {
            listItemOrder.addAll(it)
            setProductSold(it)
        }
    }

    private fun setProductSold(list: ArrayList<ItemOrder>) {
        listProductSold.clear()
        val listP = ArrayList<Product>()
        for (i in listProductAll) {
            if (i.status == "No") {
                listP.add(i)
            }
        }
        for (i in listP) {
            for (j in list) {
                if (j.product_id == i.id) {
                    for (z in listOrder) {
                        if (z.id == j.order_id) {
                            for (k in listUser) {
                                if (k.id == z.user_id){
                                    val p = ItemProductSold()
                                    p.id_order = z.id
                                    p.price = i.price?.toInt()
                                    p.name_product = i.name
                                    p.username = k.username
                                    p.image = i.photo
                                    p.date = z.create_at
                                    listProductSold.add(p)
                                }
                            }
                        }
                    }
                }
            }
        }
        setData(listProductSold)
    }

    private fun setData(list: ArrayList<ItemProductSold>) {
        var value = 0
        val pattern = "#,##0 VNĐ"
        val decimalFormat = DecimalFormat(pattern)
        binding?.tvAmount?.text = "Có ${list.size} sản phẩm đã bán"
        if(list.size>0){
            for(i in list){
                value+= i.price!!
            }
        }
        binding?.tvValue?.text = "Doanh thu: ${decimalFormat.format(value)}"
        context?.let { adapter.setData(list, it) }
        binding?.rcProduct?.adapter = adapter
        binding?.rcProduct?.layoutManager =
            GridLayoutManager(requireContext(), 1, RecyclerView.VERTICAL, false)
    }

    override fun initAction() {
        super.initAction()
        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        binding?.tvStart?.text = "2/2/2022"
        val format = SimpleDateFormat("dd/MM/yyyy")
        binding?.tvEnd?.text = "" + day + "/${month + 1}/" + year
        binding?.btnStart?.setOnClickListener {
            val dpd = context?.let {
                DatePickerDialog(it, { _, year, monthOfYear, dayOfMonth ->
                    binding?.tvStart?.text = "" + dayOfMonth + "/${monthOfYear + 1}/" + year
                }, year, month, day)
            }
            dpd?.show()
        }

        binding?.btnEnd?.setOnClickListener {
            val dpd = context?.let {
                DatePickerDialog(it, { _, year, monthOfYear, dayOfMonth ->

                    binding?.tvEnd?.text = "" + dayOfMonth + "/${monthOfYear + 1}/" + year
                }, year, month, day)
            }

            dpd?.show()
        }
        binding?.timkiem?.setOnClickListener {
            if ((format.parse(binding?.tvStart?.text.toString()) as Date) >
               (format.parse(binding?.tvEnd?.text.toString()) as Date)
            ){
                binding?.tvAmount?.text = "Ngày tháng nhập không hợp lệ"
                binding?.tvValue?.text = ""
            }
            else{
                val listNew = ArrayList<ItemProductSold>()
                for (i in listProductSold) {
                    val date = format.parse(i.date) as Date
                    if ((format.parse(binding?.tvStart?.text.toString()) as Date) <= date
                        && (format.parse(binding?.tvEnd?.text.toString()) as Date) >= date
                    ){
                        listNew.add(i)
                    }
                }
                setData(listNew)
            }
        }
    }

}
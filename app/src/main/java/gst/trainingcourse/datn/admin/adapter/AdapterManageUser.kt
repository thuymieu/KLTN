package gst.trainingcourse.datn.admin.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import gst.trainingcourse.datn.Delegate
import gst.trainingcourse.datn.R
import gst.trainingcourse.datn.databinding.ItemManageCategoryBinding
import gst.trainingcourse.datn.databinding.ItemManageUserBinding
import gst.trainingcourse.datn.model.Category
import gst.trainingcourse.datn.model.Product
import gst.trainingcourse.datn.model.User
import java.util.*
import kotlin.collections.ArrayList

class AdapterManageUser : RecyclerView.Adapter<AdapterManageUser.ItemViewHolder>() {
    private val listUse = arrayListOf<User>()
    private var listUseOld = ArrayList<User>()
    private lateinit var context: Context
    private lateinit var iOnUser: IOnClickDetail

    class ItemViewHolder(
        private val binding: ItemManageUserBinding,
        private val context: Context,
        private val iOnUser: IOnClickDetail
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(user: User) {
            binding.tvUsername.text = user.username
            binding.tvPhone.text = user.phone
            binding.btnResetPassword.setOnClickListener {
                iOnUser.onClickDetail(user)
            }
            binding.btnDelete.setOnClickListener {
                iOnUser.onClickDelete(user)
            }
        }
    }

    fun setIOnCategory(iOnUser: IOnClickDetail) {
        this.iOnUser = iOnUser
    }

    fun setData(listUpdate: List<User>, context: Context) {
        this.context = context
        listUseOld.clear()
        listUseOld.addAll(listUpdate)
        listUse.clear()
        listUse.addAll(listUpdate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding =
            ItemManageUserBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding, context, iOnUser)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(listUse[position])
    }

    override fun getItemCount(): Int {
        return listUse.size
    }

    fun getMyFilter() : Filter {
        return myFilter
    }
    private var myFilter =object: Filter() {
        //Automatic on background thread
        override fun performFiltering(charSequence: CharSequence): FilterResults {

            val filteredList: MutableList<User> = ArrayList()
            if (charSequence.isEmpty()) {
                filteredList.addAll(listUseOld)
            } else {
                for (ct in listUseOld) {
                    if (ct.username?.lowercase(Locale.getDefault())!!.contains(charSequence.toString()
                            .lowercase(Locale.getDefault()))) {
                        filteredList.add(ct)
                    }
                }
            }
            val filterResults = FilterResults()
            filterResults.values = filteredList
            return filterResults
        }

        //Automatic on UI thread
        override fun publishResults(charSequence: CharSequence, filterResults: FilterResults) {
            listUse.clear()
            listUse.addAll(filterResults.values as ArrayList<User>)
            notifyDataSetChanged()
        }
    }
}
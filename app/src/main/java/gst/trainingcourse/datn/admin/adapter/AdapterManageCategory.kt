package gst.trainingcourse.datn.admin.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import gst.trainingcourse.datn.R
import gst.trainingcourse.datn.databinding.ItemCategoryBinding
import gst.trainingcourse.datn.databinding.ItemManageCategoryBinding
import gst.trainingcourse.datn.model.Category
import gst.trainingcourse.datn.ui.adapter.AdapterCategory
import gst.trainingcourse.datn.ui.content.IOnCategory

class AdapterManageCategory : RecyclerView.Adapter<AdapterManageCategory.ItemViewHolder>() {
    private val listCategory = arrayListOf<Category>()
    private lateinit var context: Context
    private lateinit var iOnCategory: IOnClickDetail

    class ItemViewHolder(
        private val binding: ItemManageCategoryBinding,
        private val context: Context,
        private val iOnCategory: IOnClickDetail
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(category: Category) {
            binding.imCategory.setImageResource(R.drawable.ic_launcher_background)
            Glide.with(context).load(category.photo).into(binding.imCategory)
            binding.tvNameCategory.text = category.category_name
            binding.btnDetail.setOnClickListener {
                iOnCategory.onClickDetail(category)
            }
            binding.btnDelete.setOnClickListener {
                iOnCategory.onClickDelete(category)
            }
        }
    }

    fun setIOnCategory(iOnCategory: IOnClickDetail) {
        this.iOnCategory = iOnCategory
    }

    fun setData(listUpdate: List<Category>, context: Context) {
        this.context = context
        listCategory.clear()
        listCategory.addAll(listUpdate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding =
            ItemManageCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding, context, iOnCategory)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(listCategory[position])
    }

    override fun getItemCount(): Int {
        return listCategory.size
    }
}
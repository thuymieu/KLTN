package gst.trainingcourse.datn.admin.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import gst.trainingcourse.datn.R
import gst.trainingcourse.datn.databinding.ItemMamageNewsBinding
import gst.trainingcourse.datn.databinding.ItemManageCategoryBinding
import gst.trainingcourse.datn.model.Category
import gst.trainingcourse.datn.model.News

class AdapterManageNews: RecyclerView.Adapter<AdapterManageNews.ItemViewHolder>() {
    private val listNews = arrayListOf<News>()
    private lateinit var context: Context
    private lateinit var iOnDetail: IOnClickDetail

    class ItemViewHolder(
        private val binding: ItemMamageNewsBinding,
        private val context: Context,
        private val iOnDetail: IOnClickDetail
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(news: News) {
            binding.tvTitle.text =news.title
            binding.tvContent.text = news.content
            binding.tvCreatedAt.text =news.created_at
            binding.btnDetail.setOnClickListener {
                iOnDetail.onClickDetail(news)
            }
            binding.btnDelete.setOnClickListener {
                iOnDetail.onClickDelete(news)
            }
        }
    }

    fun setIOnCategory(iOnDetail: IOnClickDetail) {
        this.iOnDetail = iOnDetail
    }

    fun setData(listUpdate: List<News>, context: Context) {
        this.context = context
        listNews.clear()
        listNews.addAll(listUpdate)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding =
            ItemMamageNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding, context, iOnDetail)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(listNews[position])
    }

    override fun getItemCount(): Int {
        return listNews.size
    }
}
package gst.trainingcourse.datn.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import gst.trainingcourse.datn.databinding.ItemNewsBinding
import gst.trainingcourse.datn.model.News
import gst.trainingcourse.datn.ui.content.IOnNews

class AdapterNews : RecyclerView.Adapter<AdapterNews.ItemViewHolder>() {
    private val listNews = arrayListOf<News>()
    private lateinit var context : Context
    private lateinit var iOnNews: IOnNews

    class ItemViewHolder(private val binding: ItemNewsBinding, private val context: Context
                         , private val iOnNews: IOnNews
    ): RecyclerView.ViewHolder(binding.root) {
        fun bind(news: News) {
            binding.tvTitle.text = news.title
            binding.tvTitle.setOnClickListener {
                iOnNews.onDetail(news)
            }
        }
    }
    fun setIOnNews(iOnNews: IOnNews){
        this.iOnNews = iOnNews
    }
    fun setData(listUpdate: List<News>, context: Context) {
        this.context = context
        listNews.clear()
        listNews.addAll(listUpdate)
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemNewsBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding,context,iOnNews)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(listNews[position])
    }

    override fun getItemCount(): Int {
        return listNews.size
    }
}
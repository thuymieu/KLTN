package gst.trainingcourse.datn.admin.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import gst.trainingcourse.datn.R
import gst.trainingcourse.datn.model.Category

class AdapterCategorySelect(context: Context, resource: Int, objects: List<Category>) :
    ArrayAdapter<Category>(context, resource, objects) {
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        convertView = LayoutInflater.from(parent.context).inflate(R.layout.item_manage_select_categorry,parent,false)
        val tvSelected = convertView.findViewById<TextView>(R.id.tvSelected)
        val category =getItem(position)
        if(category != null){
            tvSelected.text = category.category_name
        }
        return convertView
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        var convertView = convertView
        convertView = LayoutInflater.from(parent.context).inflate(R.layout.item_manage_add_category,parent,false)
        val tvCategory = convertView.findViewById<TextView>(R.id.tv_category)
        val category =getItem(position)
        if(category != null){
            tvCategory.text = category.category_name
        }
        return convertView
    }
}
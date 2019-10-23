package com.life.scalebar

import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.blankj.utilcode.util.SizeUtils

class Adapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val data = mutableListOf<String>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return ViewHolder(TextView(parent.context))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val params = ViewGroup.LayoutParams( ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT)
        holder.itemView.layoutParams = params

        (holder.itemView as TextView).text = data[position]
    }

    override fun getItemCount(): Int {
        return data.size
    }


    internal class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
}

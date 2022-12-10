package com.example.wooda

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.annotation.ContentView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide


class MyViewPagerAdapter(var imageList: ArrayList<String>) :
    RecyclerView.Adapter<MyViewPagerAdapter.PagerViewHolder>() {


    inner class PagerViewHolder(parent: ViewGroup) : RecyclerView.ViewHolder
        (LayoutInflater.from(parent.context).inflate(R.layout.item_slider, parent, false)) {
        val image = itemView.findViewById<ImageView>(R.id.iv_aespa)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) = PagerViewHolder((parent))

    override fun getItemCount(): Int = imageList.size

    override fun onBindViewHolder(holder: PagerViewHolder, position: Int) {
        Glide.with(holder.image).load(imageList[position]).into(holder.image)
    }
}
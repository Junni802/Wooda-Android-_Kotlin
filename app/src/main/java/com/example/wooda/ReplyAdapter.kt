package com.example.wooda

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.wooda.databinding.BorderReplyBinding

class ReplyAdapter : RecyclerView.Adapter<Holder1>() {
    var listData = mutableListOf<String>()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder1 {
        val binding = BorderReplyBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder1(binding)
    }

    override fun onBindViewHolder(holder: Holder1, position: Int) {
        val info = listData.get(position)
        holder.setListinfo(info)
    }

    override fun getItemCount(): Int {
        return listData.size
    }
}

class Holder1 (val binding: BorderReplyBinding): RecyclerView.ViewHolder(binding.root){
    fun setListinfo(info: String){
        var arrInfo = info.split("|")
        binding.txtBoarderReply.text = arrInfo[0]
    }

}

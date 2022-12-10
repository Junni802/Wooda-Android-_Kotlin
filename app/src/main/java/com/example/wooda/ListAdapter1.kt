package com.example.wooda

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.wooda.databinding.BorderSizeBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.ArrayList

class ListAdapter1 : RecyclerView.Adapter<Holder>() {
    var listData = mutableListOf<String>()
    var id = ""
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        var img = true
        val binding = BorderSizeBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        binding.btnLove.setOnClickListener {
            if(img == true) {
                binding.btnLove.setImageResource(R.drawable.love)
                img = false
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        var bs_num = binding.txtNum.text.toString()
                        var url = URL("http://junni802.cafe24.com/boarder_insert_love.jsp?bs_num=${bs_num}&email=${id}")
                        val urlConnection = url.openConnection() as HttpURLConnection
                        urlConnection.requestMethod = "GET"
                        if (urlConnection.responseCode == HttpURLConnection.HTTP_OK) {
                            val streamReader = InputStreamReader(urlConnection.inputStream)
                            val buffered = BufferedReader(streamReader)
                            var memberList = mutableListOf<String>()
                            var result = ""
                            while (true) {
                                val line = buffered.readLine() ?: break
                                // 읽어들인 데이터가 null이면(더 이상 데이터가 없으면) 루프를 멈춤
                                if (line != "") result = line

                                // 읽어들인 데이터(한 명의 회원정보)를 리스트에 추가(빈 문자열은 제외)
                            }

                            buffered.close()
                            urlConnection.disconnect()
                            launch(Dispatchers.Main) {
                                binding.txtTotalLove.text = "${result}"
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }else{
                binding.btnLove.setImageResource(R.drawable.not_love)
                img = true
                CoroutineScope(Dispatchers.IO).launch {
                    try {
                        var bs_num = binding.txtNum.text.toString()
                        var url = URL("http://junni802.cafe24.com/boarder_delete_love.jsp?bs_num=${bs_num}&email=${id}")
                        val urlConnection = url.openConnection() as HttpURLConnection
                        urlConnection.requestMethod = "GET"
                        if (urlConnection.responseCode == HttpURLConnection.HTTP_OK) {
                            val streamReader = InputStreamReader(urlConnection.inputStream)
                            val buffered = BufferedReader(streamReader)
                            var memberList = mutableListOf<String>()
                            var result = ""
                            while (true) {
                                val line = buffered.readLine() ?: break
                                // 읽어들인 데이터가 null이면(더 이상 데이터가 없으면) 루프를 멈춤
                                if (line != "") result = line

                                // 읽어들인 데이터(한 명의 회원정보)를 리스트에 추가(빈 문자열은 제외)
                            }

                            buffered.close()
                            urlConnection.disconnect()
                            launch(Dispatchers.Main) {
                                binding.txtTotalLove.text = "${result}"
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
        }

        binding.btnReply.setOnClickListener {
            var intent = Intent(parent.context, BoarderListDetailActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.putExtra("num", "${binding.txtNum.text.toString()}")
            parent.context.startActivity(intent)
        }

        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        val info = listData.get(position)
        holder.setListInfo(info)

    }

    override fun getItemCount(): Int {
        return listData.size
    }

}

class Holder(val binding: BorderSizeBinding): RecyclerView.ViewHolder(binding.root) {
    fun setListInfo(info: String) {
        var muri = "http://junni802.cafe24.com/upload/"
        var arrInfo = info.split("|")
        binding.listTitle.text = arrInfo[0]
        binding.listSdate.text = arrInfo[1]
        binding.listEdate.text = arrInfo[2]
        binding.listContent.text = arrInfo[3]
        binding.txtNum.text = arrInfo[4]
        binding.txtTotalLove.text = arrInfo[5]
        binding.txtNum.visibility = View.INVISIBLE
        fun getAespaMembers(): ArrayList<String> {
            return arrayListOf<String>(
                "${muri}${arrInfo[6]}",
                "${muri}${arrInfo[7]}",
                "${muri}${arrInfo[8]}")
        }
        binding.img.adapter = MyViewPagerAdapter(getAespaMembers()) // 어댑터 생성
        binding.img.orientation = ViewPager2.ORIENTATION_HORIZONTAL // 방향을 가로로
    }
}
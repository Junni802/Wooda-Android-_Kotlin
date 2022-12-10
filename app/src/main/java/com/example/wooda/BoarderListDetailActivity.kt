package com.example.wooda

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wooda.databinding.ActivityBoarderListDetailBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class BoarderListDetailActivity : AppCompatActivity() {
    val binding by lazy { ActivityBoarderListDetailBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        var bs_num = intent.getStringExtra("num")

        binding.btnReplySend.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                try {
                    var url = URL("http://junni802.cafe24.com/boarder_reply_insert.jsp?bs_num=${bs_num}&bs_content=${binding.txtReply.text.toString()}")
                    val urlConnection = url.openConnection() as HttpURLConnection
                    urlConnection.requestMethod = "GET"
                    if (urlConnection.responseCode == HttpURLConnection.HTTP_OK) {
                        val streamReader = InputStreamReader(urlConnection.inputStream)
                        val buffered = BufferedReader(streamReader)
                        var content = ""
                        while (true) {
                            val line = buffered.readLine() ?: break
                            // 읽어들인 데이터가 null이면(더 이상 데이터가 없으면) 루프를 멈춤

                        }

                        buffered.close()
                        urlConnection.disconnect()
                        launch(Dispatchers.Main) {

                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            onResume()
        }



        CoroutineScope(Dispatchers.IO).launch {
            try {
                var url = URL("http://junni802.cafe24.com/boarder_reply_list.jsp?bs_num=${bs_num}")
                val urlConnection = url.openConnection() as HttpURLConnection
                urlConnection.requestMethod = "GET"
                if (urlConnection.responseCode == HttpURLConnection.HTTP_OK) {
                    val streamReader = InputStreamReader(urlConnection.inputStream)
                    val buffered = BufferedReader(streamReader)
                    var memberList = mutableListOf<String>()
                    while (true) {
                        val line = buffered.readLine() ?: break
                        // 읽어들인 데이터가 null이면(더 이상 데이터가 없으면) 루프를 멈춤
                        if (line != "") memberList.add(line)
                        // 읽어들인 데이터(한 명의 회원정보)를 리스트에 추가(빈 문자열은 제외)
                    }

                    buffered.close()
                    urlConnection.disconnect()
                    launch(Dispatchers.Main) {
                        var adapter = ReplyAdapter()
                        adapter.listData = memberList
                        binding.recyclerView.adapter = adapter
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
    }

    override fun onResume() {
        var bs_num = intent.getStringExtra("num")
        super.onResume()
        CoroutineScope(Dispatchers.IO).launch {
            try {
                var url = URL("http://junni802.cafe24.com/boarder_reply_list.jsp?bs_num=${bs_num}")

//                "http://192.168.0.68:8083/test1/boarder_reply_list.jsp?bs_num=${bs_num}"
                val urlConnection = url.openConnection() as HttpURLConnection
                urlConnection.requestMethod = "GET"
                if (urlConnection.responseCode == HttpURLConnection.HTTP_OK) {
                    val streamReader = InputStreamReader(urlConnection.inputStream)
                    val buffered = BufferedReader(streamReader)
                    var memberList = mutableListOf<String>()
                    while (true) {
                        val line = buffered.readLine() ?: break
                        // 읽어들인 데이터가 null이면(더 이상 데이터가 없으면) 루프를 멈춤
                        if (line != "") memberList.add(line)
                        // 읽어들인 데이터(한 명의 회원정보)를 리스트에 추가(빈 문자열은 제외)
                    }

                    buffered.close()
                    urlConnection.disconnect()
                    launch(Dispatchers.Main) {
                        var adapter = ReplyAdapter()
                        adapter.listData = memberList
                        binding.recyclerView.adapter = adapter
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
    }

}
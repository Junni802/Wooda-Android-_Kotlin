package com.example.wooda

import android.R
import android.app.Activity
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.wooda.databinding.FragmentHomeBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class HomeFragment : Fragment() {
    var id = ""
    lateinit var binding: FragmentHomeBinding
    override fun onAttach(context: Context) {
        super.onAttach(context)

        val activity = context as Activity
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        if (!arguments?.getString("id").equals("")) {
            id = arguments?.getString("id").toString()
        }
        binding = FragmentHomeBinding.inflate(inflater, container, false)

        val option1 = listOf<String>("전체검색", "닉네임", "제목", "지역")

        val cmdAdapter = ArrayAdapter<String>(requireContext(), android.R.layout.simple_list_item_1, option1)

        binding.schtype.adapter = cmdAdapter

        var args = ""

        binding.schtype.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if(binding.schtype.selectedItem == "닉네임")       args="?schtype=nick"
                else if(binding.schtype.selectedItem == "제목")       args="?schtype=title"
                else if(binding.schtype.selectedItem == "지역")       args="?schtype=area"
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {  }

        }

        binding.btnSearch.setOnClickListener {

            CoroutineScope(Dispatchers.IO).launch {
                try {
                    var url = URL("http://junni802.cafe24.com/boarder_list.jsp?args=${args}")
//                    "http://172.30.50.137:8083/test1/boarder_list.jsp?args=${args}"

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
                            var adapter = ListAdapter1()
                            adapter.listData = memberList
                            adapter.id = id
                            binding.boarderList.adapter = adapter
                        }
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            binding.boarderList.layoutManager = LinearLayoutManager(activity)
        }
        return binding.root
    }
}
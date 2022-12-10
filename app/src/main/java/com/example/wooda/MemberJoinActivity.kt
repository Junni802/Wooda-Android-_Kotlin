package com.example.wooda

import android.R
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Toast
import com.example.wooda.databinding.ActivityMemberJoinBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class MemberJoinActivity : AppCompatActivity() {
    var login = false
    val binding by lazy { ActivityMemberJoinBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

//
//        binding.txtMemberID.addTextChangedListener(object : TextWatcher {
//            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//
//            }
//
//            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//                var id = binding.txtMemberID.text.toString()
//                var num = 0
//                CoroutineScope(Dispatchers.IO).launch {
//                    try {
//                        var url = URL("http://192.168.219.104:8083/Wooda/member_overlap.jsp?id=${id}")
//
////                        "http://192.168.219.112:8083/test1/member_overlap.jsp?id=${id}"
//                        val urlConnection = url.openConnection() as HttpURLConnection
//                        urlConnection.requestMethod = "GET"
//                        if (urlConnection.responseCode == HttpURLConnection.HTTP_OK) {
//                            val streamReader = InputStreamReader(urlConnection.inputStream)
//                            val buffered = BufferedReader(streamReader)
//                            var result = ""
//                            while (true) {
//                                val line = buffered.readLine() ?: break
//                                // 읽어들인 데이터가 null이면(더 이상 데이터가 없으면) 루프를 멈춤
//                                if (line != "") result = line
//                                // 읽어들인 데이터(한 명의 회원정보)를 리스트에 추가(빈 문자열은 제외)
//
//                            }
//
//                            buffered.close()
//                            urlConnection.disconnect()
//                            launch(Dispatchers.Main) {
//                                if(result.equals("1")){
//                                    binding.textView14.setText("이미 사용중인 아이디 입니다.")
//                                    login = false
//                                    Log.d("login 여", "${login}")
//                                }else{
//                                    binding.textView14.setText("사용 가능한 아이디 입니다.")
//                                    login = true
//                                    Log.d("login 여", "${login}")
//                                }
//                            }
//
//                        }
//                    } catch (e: Exception) {
//                        e.printStackTrace()
//                    }
//                }
//            }
//
//            override fun afterTextChanged(p0: Editable?) {
//                var id = binding.txtMemberID.text.toString()
//                CoroutineScope(Dispatchers.IO).launch {
//                    try {
//                        var url = URL("http://192.168.219.104:8083/Wooda/member_overlap.jsp?id=${id}")
//
////                        "http://192.168.219.112:8083/test1/member_overlap.jsp?mi_mail=${id}"
//                        val urlConnection = url.openConnection() as HttpURLConnection
//                        urlConnection.requestMethod = "GET"
//                        if (urlConnection.responseCode == HttpURLConnection.HTTP_OK) {
//                            val streamReader = InputStreamReader(urlConnection.inputStream)
//                            val buffered = BufferedReader(streamReader)
//                            var result = ""
//                            while (true) {
//                                val line = buffered.readLine() ?: break
//                                // 읽어들인 데이터가 null이면(더 이상 데이터가 없으면) 루프를 멈춤
//                                if (line != "") result = line
//                                // 읽어들인 데이터(한 명의 회원정보)를 리스트에 추가(빈 문자열은 제외)
//                            }
//
//                            buffered.close()
//                            urlConnection.disconnect()
//                            launch(Dispatchers.Main) {
//                                if(result.equals("1")){
//                                    binding.textView14.setText("이미 사용중인 아이디 입니다.")
//                                    login = false
//                                }else{
//                                    binding.textView14.setText("사용 가능한 아이디 입니다.")
//                                    login = true
//                                }
//                            }
//                        }
//                    } catch (e: Exception) {
//                        e.printStackTrace()
//                    }
//                }
//            }
//
//        })
//        // 아이디 중복 체크 완료
//
//        binding.txtMemberPW2.addTextChangedListener(object : TextWatcher{
//            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//
//            }
//
//            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//                if(binding.txtMemberPW.text.toString().equals(binding.txtMemberPW2.text.toString())){
//                    binding.textView7.setText("비밀번호가 일치합니다")
//                    login = true
//                }else{
//                    binding.textView7.setText("비밀번호가 일치하지않습니다")
//                    login = false
//                }
//            }
//
//            override fun afterTextChanged(p0: Editable?) {
//                if(binding.txtMemberPW.text.toString().equals(binding.txtMemberPW2.text.toString())){
//                    binding.textView7.setText("비밀번호가 일치합니다")
//                    login = true
//                }else{
//                    binding.textView7.setText("비밀번호가 일치하지않습니다")
//                }
//            }
//
//        })
//
//        val option1 = mutableListOf<String>()
//
//        for(i in 1970..2022){
//            option1.add("${i}")
//        }
//
//        val cmdAdapter = ArrayAdapter<String>(this, R.layout.simple_list_item_1, option1)
//
//        binding.spYear.adapter = cmdAdapter
//
//
//
//        if(!binding.txtMemberName.equals("")){
//            login = true
//        }else{
//            login = false
//        }
//
//
//            binding.btnJoin.setOnClickListener {
//                var id = binding.txtMemberID.text.toString()
//                var pw = binding.txtMemberPW.text.toString()
//                var name = binding.txtMemberName.text.toString()
//                var birth = binding.spYear.selectedItem.toString()
//                CoroutineScope(Dispatchers.IO).launch {
//                    try {
//                        var url = URL("http://192.168.219.104:8083/Wooda/member_join.jsp?mi_mail=${id}&mi_nick=${name}&mi_pw=${pw}&mi_birth=${birth}")
//
//                        val urlConnection = url.openConnection() as HttpURLConnection
//                        urlConnection.requestMethod = "GET"
//                        if (urlConnection.responseCode == HttpURLConnection.HTTP_OK) {
//                            val streamReader = InputStreamReader(urlConnection.inputStream)
//                            val buffered = BufferedReader(streamReader)
//                            var result = ""
//                            while (true) {
//                                val line = buffered.readLine() ?: break
//                                // 읽어들인 데이터가 null이면(더 이상 데이터가 없으면) 루프를 멈춤
//                                if (line != "") result = line
//                                // 읽어들인 데이터(한 명의 회원정보)를 리스트에 추가(빈 문자열은 제외)
//                            }
//
//                            buffered.close()
//                            urlConnection.disconnect()
//                            Log.d("login 여", "${result}")
//                            launch(Dispatchers.Main) {
//                                if(result.equals("1")){
//                                     finish()
//                                }else{
//                                }
//                            }
//                        }
//                    } catch (e: Exception) {
//                        e.printStackTrace()
//                    }
//                }
//            }
    }
}
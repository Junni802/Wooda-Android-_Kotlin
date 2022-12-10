package com.example.wooda

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ScrollView
import android.widget.TextView
import com.example.wooda.databinding.FragmentChattingBinding
import com.google.gson.GsonBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.BufferedReader
import java.io.DataInputStream
import java.io.DataOutputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.Socket
import java.net.URL
import kotlin.concurrent.thread

class ChattingFragment : Fragment() {
    lateinit var binding: FragmentChattingBinding
    override fun onAttach(context: Context) {
        super.onAttach(context)
        val activity = context as Activity
    }

    var rcTxt: TextView? = null
    var scv: ScrollView? = null
    var dos: DataOutputStream? = null
    var pname = ""
    var nick = ""
    var id = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (!arguments?.getString("id").equals("")) {
            id = arguments?.getString("id").toString()
        }

        getNick(id)

//        Log.d("닉네임 여", "${id}")
//        Log.d("닉네임 여", "${nick}")
    }

    override fun onCreateView(

        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentChattingBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment

        rcTxt = binding.rcTxt
        scv = binding.scv

        ReceiverTh().start()

        binding.btnSend.setOnClickListener {

            Log.d("sendTxt 여", "${binding.sendTxt.text}")
            var msg = "${binding.sendTxt.text}"

            if (dos != null) {
                object : Thread() {
                    override fun run() {
                        dos!!.writeUTF(pname + msg)
                    }
                }.start()
            }


            binding.sendTxt.setText("")
            false

        }


        return binding.root

    }

    inner class ReceiverTh : Thread() {        // inner class 사용이유는 outer class 멤버변수를 사용하기 위하여 사용함
        override fun run() {        // socket을 onCreate메소드에서 만들면 에러가 나므로 run메소드를 사용하여 호출함
            var socket = Socket("192.168.219.135", 7777)

            thread(start = true) {
                Thread.sleep(3000)
                pname = "[${nick}]"
            }


            dos = DataOutputStream(socket.getOutputStream())
            var dis = DataInputStream(socket.getInputStream())

            try {

                while (dis != null) {
                    val msg = dis.readUTF()
                    activity?.runOnUiThread {
                        rcTxt!!.append(msg + "\n")
                        scv!!.fullScroll(ScrollView.FOCUS_DOWN)
                    }
                }

            } catch (e: Exception) {

            } finally {
                dis.close()
                dos!!.close()
                socket.close()
                dos = null
            }


        }
    }

    fun getNick(umail: String) {
        var gson = GsonBuilder().setLenient().create()

        var retrofit = Retrofit.Builder()
            .baseUrl("http://junni802.cafe24.com")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(OkHttpClient())
            .build()

        var chattingService = retrofit.create(ChattingNickService::class.java)

        chattingService.requestNickName("${id}").enqueue(object :
            Callback<ChattingNickData> {
            override fun onResponse(
                call: Call<ChattingNickData>,
                response: Response<ChattingNickData>
            ) {
                // 통신에 성공했을때 응답값을 받아옴
                var nickName = response.body()

                nick = nickName?.nick.toString()
                Log.d("닉네임 여", "${nick}")
            }

            override fun onFailure(call: Call<ChattingNickData>, t: Throwable) {

            }

        })
    }

}



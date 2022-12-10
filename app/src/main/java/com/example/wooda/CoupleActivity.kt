package com.example.wooda

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.core.widget.addTextChangedListener
import com.example.wooda.databinding.ActivityCoupleBinding
import com.example.wooda.databinding.ActivityMainBinding
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
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class CoupleActivity : AppCompatActivity() {

    var idChk = false
    var uemail = ""
    val binding by lazy { ActivityCoupleBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        var id = intent.getStringExtra("id").toString()

        binding.txxCoupleEmail.addTextChangedListener ( object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
//                if(id.equals(binding.txxCoupleEmail.text.toString())){
//                    idChk = false
//                }else {
//                    chkEmail(binding.txxCoupleEmail.text.toString())
//                }
//
//                Log.d("아이디 여", "${id}")
//                Log.d("체크 여", "${idChk}")

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(id.equals(binding.txxCoupleEmail.text.toString())){
                    idChk = false
                }else {
                    chkEmail(binding.txxCoupleEmail.text.toString())
                }

                Log.d("아이디 여", "${id}")
                Log.d("체크 여", "${idChk}")
            }

            override fun afterTextChanged(p0: Editable?) {
//                if(id.equals(binding.txxCoupleEmail.text.toString())){
//                    idChk = false
//                }else {
//                    chkEmail(binding.txxCoupleEmail.text.toString())
//                }
//
//                Log.d("아이디 여", "${id}")
//                Log.d("체크 여", "${idChk}")
            }

        })

        binding.btnEmailChk.setOnClickListener {
            Log.d("여부 여", "${idChk}")
            if(idChk == true){
                uemail = binding.txxCoupleEmail.text.toString()
                binding.txtCoupleChk.setText("이메일이 확인되었습니다!")


            }else{
                binding.txtCoupleChk.setText("")
                AlertDialog.Builder(this@CoupleActivity)
                    .setTitle("이메일이 정확하지 않습니다.")
                    .setMessage("이메일을 한번더 확인해 주세요")
                    .setPositiveButton("확인", object : DialogInterface.OnClickListener {
                        override fun onClick(dialog: DialogInterface, which: Int) {
                            Log.d("MyTag", "positive")
                        }
                    })
                    .setNegativeButton("cancel", object : DialogInterface.OnClickListener {
                        override fun onClick(dialog: DialogInterface, which: Int) {
                            Log.d("MyTag", "negative")
                        }
                    })
                    .create()
                    .show()

            }
        }



        binding.btnCoupleSend.setOnClickListener {      // 연인등록 신청 버튼을 클릭했을시
            if(idChk == true && binding.txtCoupleChk.text.equals("이메일이 확인되었습니다!")){
                val cal = Calendar.getInstance()
                cal.time = Date()
                val df: DateFormat = SimpleDateFormat("yyyy-MM-dd")
                cal.add(Calendar.DATE, 3)
                var edate = df.format(cal.time).toString()      // 현재날짜에 3일을 추가한 날짜(연인신청 삭제 날짜)

                coupleSend(id, uemail, edate)
                Log.d("me 여", "${id}")
                Log.d("you 여", "${uemail}")
                Log.d("me 여", "${edate}")

            }else{
                AlertDialog.Builder(this@CoupleActivity)
                    .setTitle("이메일이 정확하지 않습니다.")
                    .setMessage("이메일을 한번더 확인해 주세요")
                    .setPositiveButton("확인", object : DialogInterface.OnClickListener {
                        override fun onClick(dialog: DialogInterface, which: Int) {
                            Log.d("MyTag", "positive")
                        }
                    })
                    .setNegativeButton("cancel", object : DialogInterface.OnClickListener {
                        override fun onClick(dialog: DialogInterface, which: Int) {
                            Log.d("MyTag", "negative")
                        }
                    })
                    .create()
                    .show()

            }

        }


    }

    fun chkEmail(uid: String) {
        var gson = GsonBuilder().setLenient().create()

        var retrofit = Retrofit.Builder()
            .baseUrl("http://junni802.cafe24.com")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(OkHttpClient())
            .build()

        var mailChk = retrofit.create(CoupleApplyService::class.java)

        mailChk.requestExitEmail("${uid}").enqueue(object :
            Callback<CoupleApplyData> {
            override fun onResponse(
                call: Call<CoupleApplyData>,
                response: Response<CoupleApplyData>
            ) {
                // 통신에 성공했을때 응답값을 받아옴
                var num = response.body()
                if (num?.num.toString().equals("1")) idChk = true else idChk = false
                Log.d("이메일123 여", "${num?.num}")
            }

            override fun onFailure(call: Call<CoupleApplyData>, t: Throwable) {

            }

        })
    }


    fun coupleSend(sMail: String, rMAil: String, eDate: String){
        var gson = GsonBuilder().setLenient().create()

        var retrofit = Retrofit.Builder()
            .baseUrl("http://junni802.cafe24.com")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(OkHttpClient())
            .build()

        var send = retrofit.create(SendCoupleService::class.java)

        send.requestSendCouple("${sMail}", "${rMAil}", "${eDate}").enqueue(object :
            Callback<SendCoupleData> {
            override fun onResponse(
                call: Call<SendCoupleData>,
                response: Response<SendCoupleData>
            ) {
                // 통신에 성공했을때 응답값을 받아옴
                var num = response.body()
                if (num?.num.toString().equals("1")) idChk = true else idChk = false
                Log.d("이메일 여", "${num?.num}")
            }

            override fun onFailure(call: Call<SendCoupleData>, t: Throwable) {

            }

        })
    }

}
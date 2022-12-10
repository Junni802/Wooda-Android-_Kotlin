package com.example.wooda

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.util.Patterns
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.core.widget.doOnTextChanged
import com.example.wooda.databinding.ActivityTestBinding
import com.google.gson.GsonBuilder
import kotlinx.coroutines.*
import okhttp3.OkHttpClient
import org.w3c.dom.Text
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

class TestActivity : AppCompatActivity() {
    // 모든 조건이 빈 문자열이 아니면 버튼 클릭시키기
    var loginID = false
    var loginPW = false
    var loginName = false
    var loginNick = false
    var idChk = false
    var nickChk = false
    var mail = ""
    var can = false
    var th = false
    val binding by lazy { ActivityTestBinding.inflate(layoutInflater) }

    // 타이머
    var total = 180    // 전체 시간을 저장할 변수
    var started = false    // 시작 여부를 체크할 변수


    var handler = object : Handler(Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            val minute = String.format("%02d", total / 60)
            val second = String.format("%02d", total % 60)
            binding.txtTimer.text = "$minute:$second"
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.txtMemberID.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                chkEmail(binding.txtMemberID.text.toString())
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                chkEmail(binding.txtMemberID.text.toString())
            }

            override fun afterTextChanged(p0: Editable?) {

            }

        })

        binding.btnIdCiteNumber.setOnClickListener {            // 이메일 인증 버튼을 클릭시
            if (!Patterns.EMAIL_ADDRESS.matcher(binding.txtMemberID.text.toString())
                    .matches()
            ) {
                Toast.makeText(this@TestActivity, "이메일을 입력해주세요.", Toast.LENGTH_SHORT).show()
            } else if (idChk == false) {        // 이메일 중복확인 조건문
                Log.d("이메일 2번", "${idChk}")
                Toast.makeText(this@TestActivity, "이미 사용중인 이메일입니다.", Toast.LENGTH_SHORT)
                    .show()
            } else {
                binding.txtCiteNumber.isVisible = true
                binding.btnMemberIdCite.isVisible = true
                binding.txtTimer.isVisible = true
                binding.textView14.isVisible = true
                binding.txtMemberID.isEnabled = false
                binding.btnIdCiteNumber.text = "다시 인증하기"
                sendMail1(binding.txtMemberID.text.toString())

                thread(start = true) {
                    handler?.removeCallbacksAndMessages(null)
                    while (started) {
                        Thread.sleep(1000)
                        total -= 1
                        handler?.sendEmptyMessage(0)
                        // handler를 호출하는 함수로 값은 큰 의미가 없음
                    }
                }

                if (started == true) {
                    started = false
                    total = 180
                    binding.txtMemberID.setText("")
                    binding.txtCiteNumber.setText("")
                    binding.textView14.text = ""
                    binding.txtMemberID.isEnabled = true
                    binding.txtCiteNumber.isVisible = false
                    binding.btnMemberIdCite.isVisible = false
                    binding.txtTimer.isVisible = false
                    binding.textView14.isVisible = false
                    binding.btnIdCiteNumber.text = "인증번호 받기"
                } else {
                    started = true
                }
                Log.d("메일 여", "${mail}")

            }

            if (total != 0) {
                binding.btnMemberIdCite.setOnClickListener {
                    if ("${mail}".equals("${binding.txtCiteNumber.text}")) {
                        binding.textView14.text = "인증이 완료되었습니다!"
                        loginID = true
                        started = false
                        binding.txtMemberID.isEnabled = false
                        binding.txtCiteNumber.isEnabled = false
                        binding.btnIdCiteNumber.setText("인증")
                    } else {
                        Log.d("메일 여", "${mail}")
                        binding.textView14.text = "인증번호가 잘못되었습니다."
                    }
                }
            }
        }


        // 이름 Part -------------------------------------------------------------
        if (!binding.txtMemberName.equals("")) loginName = true else loginName = false
        // --------------------------- 이름 Part 종료 -------------------------------------


        // 비밀번호 Part
        binding.txtMemberPW2.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {

            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (binding.txtMemberPW.text.toString()
                        .equals(binding.txtMemberPW2.text.toString())
                ) {
                    binding.textView7.setText("비밀번호가 일치합니다")
                    loginPW = true
                } else {
                    binding.textView7.setText("비밀번호가 일치하지않습니다")
                    loginPW = false
                }
            }

            override fun afterTextChanged(p0: Editable?) {
                if (binding.txtMemberPW.text.toString()
                        .equals(binding.txtMemberPW2.text.toString())
                ) {
                    binding.textView7.setText("비밀번호가 일치합니다")
                    loginPW = true
                } else {
                    binding.textView7.setText("비밀번호가 일치하지않습니다")
                    loginPW = false
                }
            }

        })
        // --------------------------- 비밀번호 Part 종료 -------------------------------------

        // 닉네임 Part

        binding.txtMemberNick.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                chkNick(binding.txtMemberNick.text.toString())
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                chkNick(binding.txtMemberNick.text.toString())
            }

            override fun afterTextChanged(p0: Editable?) {

            }
        })

        binding.btnCiteNick.setOnClickListener {

            if (nickChk == true) {
                loginNick = true
                Toast.makeText(this, "사용가능한 닉네임입니다.", Toast.LENGTH_SHORT).show()
                Log.d("닉네임true 여", "${loginNick}")
            } else {
                loginNick = false
                Toast.makeText(this, "이미 사용중인 닉네임입니다.", Toast.LENGTH_SHORT).show()
                Log.d("닉네임false 여", "${loginNick}")
            }

        }
        // ----------------------------- 닉네임 Part End.. -----------------------------

        val option1 = mutableListOf<String>()

        for (i in 1970..2022) {
            option1.add("${i}")
        }

        val cmdAdapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, option1)

        binding.spYear.adapter = cmdAdapter


        loginID == true
        loginNick == true
        loginPW == true
        loginName == true


//        if (loginID == true && loginNick == true && loginPW == true && loginName == true) {
        binding.btnJoin.setOnClickListener {
            var id = binding.txtMemberID.text.toString()
            var name = binding.txtMemberName.text.toString()
            var pw = binding.txtMemberPW.text.toString()
            var nick = binding.txtMemberNick.text.toString()
            var birth = binding.spYear.selectedItem.toString()

            memberJoin(id, name, nick, pw, birth)
        }
    }

    fun memberJoin(uid: String, uname: String, unick: String, upw: String, ubirth: String) {
        var gson = GsonBuilder().setLenient().create()

        var retrofit = Retrofit.Builder()
            .baseUrl("http://junni802.cafe24.com")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(OkHttpClient())
            .build()

        var memberJoin = retrofit.create(MemberJoinService::class.java)

        memberJoin.requestMemberJoin("${uid}", "${uname}", "${unick}", "${upw}", "${ubirth}")
            .enqueue(object :
                Callback<MemberJoinData> {
                override fun onResponse(
                    call: Call<MemberJoinData>,
                    response: Response<MemberJoinData>
                ) {
                    // 통신에 성공했을때 응답값을 받아옴
                    var nickName = response.body()

                    var num = nickName?.num.toString()
                    Log.d("닉네임 여", "${num}")
                    if (num.equals("1")) {
                        Toast.makeText(this@TestActivity, "회원가입이 완료되었습니다", Toast.LENGTH_SHORT)
                            .show()
                        finish()
                    } else {
                        Toast.makeText(this@TestActivity, "다시한번 확인해주세요.ㅅ", Toast.LENGTH_SHORT)
                            .show()
                    }
                }

                override fun onFailure(call: Call<MemberJoinData>, t: Throwable) {

                }

            })
    }

    fun sendMail1(uRecieve: String) {
        var gson = GsonBuilder().setLenient().create()

        var retrofit = Retrofit.Builder()
            .baseUrl("http://junni802.cafe24.com")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(OkHttpClient())
            .build()

        var sendMail = retrofit.create(JoinSendMailService::class.java)

        sendMail.requestSendMail("${uRecieve}").enqueue(object :
            Callback<JoinSendMailData> {
            override fun onResponse(
                call: Call<JoinSendMailData>,
                response: Response<JoinSendMailData>
            ) {
                // 통신에 성공했을때 응답값을 받아옴
                var num = response.body()
                Log.d("닉네임 여", "${num?.num}")
                mail = num?.num.toString()
            }

            override fun onFailure(call: Call<JoinSendMailData>, t: Throwable) {

            }
        })
    }

    fun chkEmail(uid: String) {
        var gson = GsonBuilder().setLenient().create()

        var retrofit = Retrofit.Builder()
            .baseUrl("http://junni802.cafe24.com")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(OkHttpClient())
            .build()

        var mailChk = retrofit.create(MemberJoinEmailChkService::class.java)

        mailChk.requestEmailChk("${uid}").enqueue(object :
            Callback<MemberJoinEmailChkData> {
            override fun onResponse(
                call: Call<MemberJoinEmailChkData>,
                response: Response<MemberJoinEmailChkData>
            ) {
                // 통신에 성공했을때 응답값을 받아옴
                var num = response.body()
                if (num?.num.toString().equals("1")) idChk = false else idChk = true
                Log.d("이메일 여", "${num?.num}")
            }

            override fun onFailure(call: Call<MemberJoinEmailChkData>, t: Throwable) {

            }

        })
    }


    fun chkNick(nickName: String) {
        var gson = GsonBuilder().setLenient().create()

        var retrofit = Retrofit.Builder()
            .baseUrl("http://junni802.cafe24.com")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(OkHttpClient())
            .build()

        var nickChk1 = retrofit.create(MemberJoinNickChkService::class.java)

        nickChk1.requestNickChk("${nickName}").enqueue(object :
            Callback<MemberJoinNickChkData> {
            override fun onResponse(
                call: Call<MemberJoinNickChkData>,
                response: Response<MemberJoinNickChkData>
            ) {
                // 통신에 성공했을때 응답값을 받아옴
                var num = response.body()
                Log.d("닉네임 여", "${num?.num}")
                if (num?.num.equals("0")) {
                    nickChk = true
                } else {
                    nickChk = false
                }
            }

            override fun onFailure(call: Call<MemberJoinNickChkData>, t: Throwable) {

            }

        })
    }

}
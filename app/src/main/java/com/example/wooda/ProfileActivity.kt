package com.example.wooda

import android.opengl.Visibility
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.view.isVisible
import com.example.wooda.databinding.ActivityProfileBinding
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

class ProfileActivity : AppCompatActivity() {
    val binding by lazy { ActivityProfileBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        var id = intent.getStringExtra("id").toString()
        Log.d("id12 여", "${id}")

        memberInfo(id)  // 회원정보를 가져오는 메소드


        binding.btnPwChangeGo.setOnClickListener {
            if ((binding.txtProfilePW.text.toString()).equals(binding.txtProfilePW1.text.toString())) {
                binding.txtProfilePW.isVisible = false
                binding.txtProfileNewPw1.isVisible = true
                binding.txtProfileNewPw2.isVisible = true
                binding.btnPwChange.isVisible = true
            } else {
                Toast.makeText(this, "비밀번호가 올바르지 않습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnPwChange.setOnClickListener {
            if ((binding.txtProfileNewPw1.text.toString()).equals(binding.txtProfileNewPw2.text.toString())) {
                pwChange(id, binding.txtProfileNewPw1.text.toString())  // 비밀번호 변경 함수
            } else {
                Toast.makeText(this, "비밀번호가 올바르지 않습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        binding.btnNameChange.setOnClickListener {

            NickChange(id, binding.txtProfileName.text.toString())


//            CoroutineScope(Dispatchers.IO).launch {
//                try {
//                    var url =
//                        URL("http://junni802.cafe24.com/update_nick.jsp?mi_mail=${id}&mi_nick=${binding.txtProfileName.text}")
//                    val urlConnection = url.openConnection() as HttpURLConnection
//                    urlConnection.requestMethod = "GET"
//                    if (urlConnection.responseCode == HttpURLConnection.HTTP_OK) {
//                        val streamReader = InputStreamReader(urlConnection.inputStream)
//                        val buffered = BufferedReader(streamReader)
//                        var result = ""
//                        while (true) {
//                            val line = buffered.readLine() ?: break
//                            // 읽어들인 데이터가 null이면(더 이상 데이터가 없으면) 루프를 멈춤
//                            if (line != "") result = line
//                            // 읽어들인 데이터(한 명의 회원정보)를 리스트에 추가(빈 문자열은 제외)
//                        }
//
//                        buffered.close()
//                        urlConnection.disconnect()
//                        launch(Dispatchers.Main) {
//                            if (result.equals("1")) {
//                                Toast.makeText(
//                                    this@ProfileActivity,
//                                    "닉네임이 성공적으로 변경되었습니다.",
//                                    Toast.LENGTH_SHORT
//                                ).show()
//                                onResume()
//                            } else {
//                                Toast.makeText(
//                                    this@ProfileActivity,
//                                    "다시시도해주세요.",
//                                    Toast.LENGTH_SHORT
//                                ).show()
//                            }
//                        }
//                    }
//                } catch (e: Exception) {
//                    e.printStackTrace()
//                }
//            }
        }

        binding.btnProfileBack.setOnClickListener {
            finish()
        }

    }

    override fun onResume() {
        super.onResume()
    }

    // ------------------------------- 회원정보 함수 ----------------------------
    fun memberInfo(uid: String) {       // 회원정보를 추출하는 함수
        var gson = GsonBuilder().setLenient().create()

        var retrofit = Retrofit.Builder()
            .baseUrl("http://junni802.cafe24.com")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(OkHttpClient())
            .build()

        var memberInfo = retrofit.create(ProfileMemberService::class.java)

        memberInfo.requestMemberInfo("${uid}").enqueue(object :
            Callback<ProfileMemberData> {
            override fun onResponse(
                call: Call<ProfileMemberData>,
                response: Response<ProfileMemberData>
            ) {
                binding.txtProfileID.setText(response.body()?.mi_mail)
                binding.txtProfilePW1.setText(response.body()?.mi_pw)
                binding.txtProfileName1.setText(response.body()?.mi_nick)
                binding.txtProfileName.setText(response.body()?.mi_nick)
                Log.d("아이디여부 여", "${uid}")
                Log.d("통신여부 여", "${response.body()}")

            }

            override fun onFailure(call: Call<ProfileMemberData>, t: Throwable) {
                Log.d("통신여부 여", "실패ㄱ")
            }

        })
    }
// ------------------------------------ End -----------------------------------

    // ---------------------------- 비밀번호 변경 함수 -------------------------------
    fun pwChange(uid: String, upw: String) {
        var gson = GsonBuilder().setLenient().create()

        var retrofit = Retrofit.Builder()
            .baseUrl("http://junni802.cafe24.com")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(OkHttpClient())
            .build()

        var pwChange = retrofit.create(ProfilePwChangeService::class.java)

        pwChange.requestChangePW("${uid}", upw).enqueue(object :
            Callback<ProfilePwChangeData> {
            override fun onResponse(
                call: Call<ProfilePwChangeData>,
                response: Response<ProfilePwChangeData>
            ) {
                var num = response.body()?.num

                if (num.equals("1")) {
                    Toast.makeText(
                        this@ProfileActivity,
                        "비밀번호가 성공적으로 변경되었습니다.",
                        Toast.LENGTH_SHORT
                    ).show()
                    binding.txtProfilePW.setText("")
                    binding.txtProfilePW.isVisible = true
                    binding.txtProfileNewPw1.isVisible = false
                    binding.txtProfileNewPw2.isVisible = false
                    binding.btnPwChange.isVisible = false
                } else {
                    Toast.makeText(
                        this@ProfileActivity,
                        "다시시도해주세요.",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                Log.d("번호 여", "${num}")

            }

            override fun onFailure(call: Call<ProfilePwChangeData>, t: Throwable) {
                Log.d("통신여부 여", "실패ㄱ")
            }

        })
    }
// -------------------------------------- End -----------------------------------------------

    fun NickChange(uid: String, nick: String){
        var gson = GsonBuilder().setLenient().create()

        var retrofit = Retrofit.Builder()
            .baseUrl("http://junni802.cafe24.com")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(OkHttpClient())
            .build()

        var nickChange = retrofit.create(ProfileNickChangeService::class.java)

        nickChange.requestChangeNick("${uid}", nick).enqueue(object :
            Callback<ProfileNickChangeData> {
            override fun onResponse(
                call: Call<ProfileNickChangeData>,
                response: Response<ProfileNickChangeData>
            ) {
                var num = response.body()?.num

                Log.d("번호 여", "${num}")

            }

            override fun onFailure(call: Call<ProfileNickChangeData>, t: Throwable) {
                Log.d("통신여부 여", "실패ㄱ")
            }

        })
    }
}
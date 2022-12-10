package com.example.wooda

import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AlertDialog
import com.example.wooda.databinding.ActivityMainBinding
import com.example.wooda.databinding.ActivityWoodaBinding
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

class MainActivity : AppCompatActivity() {
    val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if (MySharedPreferences.getUserId(this).isNullOrBlank() || MySharedPreferences.getUserPass(
                this
            ).isNullOrBlank()
        ) {
            binding.btnLogin.setOnClickListener {       // 로그인 버튼을 클릭 했을시
                Login()
            }
        } else {
            val intent = Intent(this, WoodaActivity::class.java)
            startActivity(intent)
        }

        binding.btnMemberJoin.setOnClickListener {
            var intent = Intent(this, TestActivity::class.java)



            startActivity(intent)
        }

    }

    fun Login() {
        val intent = Intent(this, WoodaActivity::class.java)
        var gson = GsonBuilder().setLenient().create()

        var retrofit = Retrofit.Builder()
            .baseUrl("http://junni802.cafe24.com")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(OkHttpClient())
            .build()

        var loginService = retrofit.create(WoodaLoginServiceChk::class.java)

//        Log.d("왜 여", "${binding.txtID.text.toString()}")
//        Log.d("왜 여", "${binding.txtPW.text.toString()}")
        loginService.requestLogin("${binding.txtID.text.toString()}", "${binding.txtPW.text.toString()}").enqueue(object :
            Callback<WoodaLoginData> {
            override fun onResponse(call: Call<WoodaLoginData>, response: Response<WoodaLoginData>) {
                // 통신에 성공했을때 응답값을 받아옴
                var login = response.body()

                Log.d("통신성공 여", "${login}")   // 통신이 잘되는지 확인하는 log

                if(login?.chk.equals("1")){
                    intent.putExtra("mi_id","${binding.txtID.text}")
                    startActivity(intent)
                }else{
                    val builder = AlertDialog.Builder(this@MainActivity)
                    builder.setTitle("로그인에 실패했습니다.")
                        .setMessage("아이디 혹은 비밀번호가 일치하지 않습니다")
                        .setPositiveButton("확인",
                        DialogInterface.OnClickListener { dialog, id ->
                    })
                    builder.show()
                }
            }

            override fun onFailure(call: Call<WoodaLoginData>, t: Throwable) {
                // 웹통신에 실패했을때 실행되는 코드
                Log.d("통신실패 여", "${t}")
            }

        })

    }
}

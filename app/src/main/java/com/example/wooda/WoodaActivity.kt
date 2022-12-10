package com.example.wooda

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.isVisible
import com.example.wooda.databinding.ActivityWoodaBinding
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WoodaActivity : AppCompatActivity() {
    var id = ""
    val binding by lazy { ActivityWoodaBinding.inflate(layoutInflater) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        if(intent.getStringExtra("mi_id").toString().equals("null") || intent.getStringExtra("mi_id").toString().equals("")){
            id = MySharedPreferences.getUserId(this)
        }else{
            id = intent.getStringExtra("mi_id").toString()
            onResume()
        }

        binding.btnHome.setOnClickListener {
            binding.btnHome.setBackgroundColor(resources.getColor(R.color.teal_200))
            binding.btnWrite.setBackgroundColor(resources.getColor(R.color.white))
            binding.btnChatting.setBackgroundColor(resources.getColor(R.color.white))
            binding.btnMyPage.setBackgroundColor(resources.getColor(R.color.white))
            val homeFragment: HomeFragment = HomeFragment()
            val bundle = Bundle()
            bundle.putString("id", "${id}")
            homeFragment.arguments = bundle
            val trans = supportFragmentManager.beginTransaction()
            trans.add(R.id.woodaFrame, homeFragment)
            trans.commit()
        }

        binding.btnWrite.setOnClickListener {
            binding.btnHome.setBackgroundColor(resources.getColor(R.color.white))
            binding.btnWrite.setBackgroundColor(resources.getColor(R.color.teal_200))
            binding.btnChatting.setBackgroundColor(resources.getColor(R.color.white))
            binding.btnMyPage.setBackgroundColor(resources.getColor(R.color.white))
            val boarderWriteFragment: BoarderWriteFragment = BoarderWriteFragment()
            val bundle = Bundle()
            bundle.putString("id", "${id}")
            boarderWriteFragment.arguments = bundle
            val trans = supportFragmentManager.beginTransaction()
            trans.add(R.id.woodaFrame, boarderWriteFragment)
            trans.commit()
        }

        binding.btnChatting.setOnClickListener {
            binding.btnHome.setBackgroundColor(resources.getColor(R.color.white))
            binding.btnWrite.setBackgroundColor(resources.getColor(R.color.white))
            binding.btnChatting.setBackgroundColor(resources.getColor(R.color.teal_200))
            binding.btnMyPage.setBackgroundColor(resources.getColor(R.color.white))
            val chattingFragment: ChattingFragment = ChattingFragment()
            val bundle = Bundle()
            bundle.putString("id", "${id}")
            chattingFragment.arguments = bundle
            val trans = supportFragmentManager.beginTransaction()
            trans.add(R.id.woodaFrame, chattingFragment)
            trans.commit()
        }

        binding.btnMyPage.setOnClickListener {
            binding.btnHome.setBackgroundColor(resources.getColor(R.color.white))
            binding.btnWrite.setBackgroundColor(resources.getColor(R.color.white))
            binding.btnChatting.setBackgroundColor(resources.getColor(R.color.white))
            binding.btnMyPage.setBackgroundColor(resources.getColor(R.color.teal_200))
            val mypageFragment: MypageFragment = MypageFragment()
            val bundle = Bundle()
            bundle.putString("id", "${id}")
            mypageFragment.arguments = bundle
            val trans = supportFragmentManager.beginTransaction()
            trans.add(R.id.woodaFrame, mypageFragment)
            trans.commit()
        }

    }

    private fun homeFragment(){
        val homeFragment: HomeFragment = HomeFragment()
        val trans = supportFragmentManager.beginTransaction()
        trans.add(R.id.woodaFrame, homeFragment)
        trans.commit()
    }

    override fun onResume() {
        super.onResume()
        var gson = GsonBuilder().setLenient().create()

        var retrofit = Retrofit.Builder()
            .baseUrl("http://junni802.cafe24.com")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(OkHttpClient())
            .build()

        var recieve = retrofit.create(RecieveCoupleService::class.java)
        Log.d("이메일 여", "gd")

        recieve.requestExitEmail("${id}").enqueue(object :
            Callback<RecieveCoupleData> {
            override fun onResponse(
                call: Call<RecieveCoupleData>,
                response: Response<RecieveCoupleData>
            ) {
                // 통신에 성공했을때 응답값을 받아옴
                var num = response.body()
                Log.d("이메일 여", "${num?.cm_mail_s}")
                if (!num?.cm_mail_s.equals("") && num?.cm_mail_s != null){
                    Log.d("연인 여", "있음")
                    AlertDialog.Builder(this@WoodaActivity)
                        .setTitle("연인요청이 등록되었습니다.")
                        .setMessage("${num?.cm_mail_s}님이 연인신청을 보냈습니다")
                        .setNegativeButton("거절", object : DialogInterface.OnClickListener {
                            override fun onClick(dialog: DialogInterface, which: Int) {
                                Log.d("MyTag", "negative")
                                var gson = GsonBuilder().setLenient().create()

                                var retrofit = Retrofit.Builder()
                                    .baseUrl("http://junni802.cafe24.com")
                                    .addConverterFactory(GsonConverterFactory.create(gson))
                                    .client(OkHttpClient())
                                    .build()

                                var refuse = retrofit.create(CoupleSendRefuseService::class.java)

                                refuse.requestCoupleRefuse("${id}").enqueue(object :
                                    Callback<CoupleSendRefuseData> {
                                    override fun onResponse(
                                        call: Call<CoupleSendRefuseData>,
                                        response: Response<CoupleSendRefuseData>
                                    ) {
                                        var num = response.body()?.num

                                        Log.d("번호 여", "${num}")

                                    }

                                    override fun onFailure(call: Call<CoupleSendRefuseData>, t: Throwable) {
                                        Log.d("통신여부 여", "실패ㄱ")
                                    }

                                })
                            }
                        })
                        .setPositiveButton("수락", object : DialogInterface.OnClickListener {
                            override fun onClick(dialog: DialogInterface, which: Int) {
                                Log.d("MyTag", "positive")

                                var gson = GsonBuilder().setLenient().create()

                                var retrofit = Retrofit.Builder()
                                    .baseUrl("http://junni802.cafe24.com")
                                    .addConverterFactory(GsonConverterFactory.create(gson))
                                    .client(OkHttpClient())
                                    .build()

                                var accept = retrofit.create(CoupleSendAcceptService::class.java)

                                accept.requestCoupleAccept("${id}" ,"${num?.cm_mail_s}").enqueue(object :
                                    Callback<CoupleSendAcceptData> {
                                    override fun onResponse(
                                        call: Call<CoupleSendAcceptData>,
                                        response: Response<CoupleSendAcceptData>
                                    ) {
                                        var num = response.body()?.num

                                        Log.d("번호 여", "${num}")

                                    }

                                    override fun onFailure(call: Call<CoupleSendAcceptData>, t: Throwable) {
                                        Log.d("통신여부 여", "실패ㄱ")
                                    }

                                })
                            }
                        })
                        .create()
                        .show()
                }else{
                    Log.d("연인 여", "없음")
                }

            }

            override fun onFailure(call: Call<RecieveCoupleData>, t: Throwable) {

            }

        })
    }

//    private fun chattingFragment(){
//        val chattingFragment: ChattingFragment = ChattingFragment()
//        val trans = supportFragmentManager.beginTransaction()
//        trans.add(R.id.woodaFrame, chattingFragment)
//        trans.commit()
//    }

//    fun setFragment() {
//        val senderFragment: SenderFragment = SenderFragment()
//        val receiverFragment: ReceiverFragment = ReceiverFragment()
//        val trans = supportFragmentManager.beginTransaction()
//        trans.add(R.id.frameLayout1, senderFragment)
//        trans.add(R.id.frameLayout2, receiverFragment)
//        trans.commit()
//    }
}
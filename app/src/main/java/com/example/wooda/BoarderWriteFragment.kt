package com.example.wooda

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.core.view.isVisible
import com.example.wooda.databinding.FragmentBoarderWriteBinding
import com.example.wooda.databinding.FragmentHomeBinding
import com.google.gson.GsonBuilder
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class BoarderWriteFragment : Fragment() {
    var via1X = "0"
    var via1Y = "0"
    var via1Name = ""
    var via2X = "0"
    var via2Y = "0"
    var via2Name = ""
    var via3X = "0"
    var via3Y = "0"
    var via3Name = ""
    var nick = ""
    var id = ""
    var bitmap: Bitmap? = null
    var img1Name = ""
    var img2Name = ""
    var img3Name = ""

    companion object {
        const val CHOOSE_PICTURE1 = 1
        const val CHOOSE_PICTURE2 = 2
        const val CHOOSE_PICTURE3 = 3
    }

    lateinit var binding: FragmentBoarderWriteBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBoarderWriteBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        if (!arguments?.getString("id").equals("")) {
            id = arguments?.getString("id").toString()
        }
        getNick(id)

        var data = listOf(
            "??????",
            "??????",
            "??????",
            "??????",
            "??????",
            "??????",
            "??????",
            "??????",
            "??????",
            "??????",
            "??????",
            "??????",
            "??????",
            "??????",
            "??????",
            "??????",
            "??????",
            "??????"
        )
        // spinner??? ?????? ????????? ??????
        var adapter =
            ArrayAdapter<String>(requireContext(), android.R.layout.simple_list_item_1, data)

        binding.btnBoarderSpinner.adapter = adapter

        val option1 = mutableListOf<String>()

        for (i in 1970..2022) {
            option1.add("${i}")
        }

        val yearAdapter =
            ArrayAdapter<String>(requireContext(), android.R.layout.simple_list_item_1, option1)

        binding.spnSYear.adapter = yearAdapter   // ???????????? ??????
        binding.spnEYear.adapter = yearAdapter   // ???????????? ??????

        val month = mutableListOf<String>()

        for (i in 1..12) {
            month.add("${i}")
        }

        var monthAdapter =
            ArrayAdapter<String>(requireContext(), android.R.layout.simple_list_item_1, month)

        binding.spnSMonth.adapter = monthAdapter
        binding.spnEMonth.adapter = monthAdapter

        binding.spnSYear.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

                val option2 = mutableListOf<String>()

                for (i in option1[p2].toInt()..2022) {
                    option2.add("${i}")
                }

                val yearAdapter = ArrayAdapter<String>(
                    requireContext(),
                    android.R.layout.simple_list_item_1,
                    option2
                )

                binding.spnEYear.adapter = yearAdapter

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }

        binding.spnSMonth.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }

        binding.spnSMonth.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (month[p2].equals("1") || month[p2].equals("3") || month[p2].equals("5") || month[p2].equals(
                        "7"
                    ) || month[p2].equals("8") || month[p2].equals("10") || month[p2].equals("12")
                ) {
                    val date = mutableListOf<String>()

                    for (i in 1..31) {
                        date.add("${i}")
                    }

                    var dateAdapter = ArrayAdapter<String>(
                        requireContext(),
                        android.R.layout.simple_list_item_1,
                        date
                    )

                    binding.spnSDate.adapter = dateAdapter
                } else if (month[p2].equals("4") || month[p2].equals("6") || month[p2].equals("9") || month[p2].equals(
                        "11"
                    )
                ) {
                    val date = mutableListOf<String>()

                    for (i in 1..30) {
                        date.add("${i}")
                    }

                    var dateAdapter = ArrayAdapter<String>(
                        requireContext(),
                        android.R.layout.simple_list_item_1,
                        date
                    )
                    binding.spnSDate.adapter = dateAdapter
                } else if (month[p2].equals("2")) {
                    val date = mutableListOf<String>()

                    for (i in 1..28) {
                        date.add("${i}")
                    }

                    var dateAdapter = ArrayAdapter<String>(
                        requireContext(),
                        android.R.layout.simple_list_item_1,
                        date
                    )
                    binding.spnSDate.adapter = dateAdapter
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }

        binding.btnMap1.setOnClickListener {
            val intent = Intent(context, KaKaoMapActivity::class.java)
            startActivityForResult(intent, 1234)
        }

//        if(!binding.btnMap1.text.equals("?????????1")){
//            binding.btnMap2.isEnabled = false
//        }
//
//        if(!binding.btnMap2.text.equals("?????????2")){
//            binding.btnMap3.isEnabled = false
//        }


        binding.btnMap2.setOnClickListener {
            val intent = Intent(context, KaKaoMapActivity::class.java)
            startActivityForResult(intent, 12345)
        }

        binding.btnMap3.setOnClickListener {
            val intent = Intent(context, KaKaoMapActivity::class.java)
            startActivityForResult(intent, 123456)
        }

        binding.btnImage1.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(intent, CHOOSE_PICTURE1)
        }

        binding.btnImage2.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(intent, CHOOSE_PICTURE2)
        }


        binding.btnImage3.setOnClickListener {
            val intent = Intent()
            intent.type = "image/*"
            intent.action = Intent.ACTION_GET_CONTENT
            startActivityForResult(intent, CHOOSE_PICTURE3)
        }



        binding.spnEMonth.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(p0: AdapterView<*>?, p1: View?, p2: Int, p3: Long) {
                if (month[p2].equals("1") || month[p2].equals("3") || month[p2].equals("5") || month[p2].equals(
                        "7"
                    ) || month[p2].equals("8") || month[p2].equals("10") || month[p2].equals("12")
                ) {
                    val date = mutableListOf<String>()

                    for (i in 1..31) {
                        date.add("${i}")
                    }

                    var dateAdapter = ArrayAdapter<String>(
                        requireContext(),
                        android.R.layout.simple_list_item_1,
                        date
                    )

                    binding.spnEDate.adapter = dateAdapter
                } else if (month[p2].equals("4") || month[p2].equals("6") || month[p2].equals("9") || month[p2].equals(
                        "11"
                    )
                ) {
                    val date = mutableListOf<String>()

                    for (i in 1..30) {
                        date.add("${i}")
                    }

                    var dateAdapter = ArrayAdapter<String>(
                        requireContext(),
                        android.R.layout.simple_list_item_1,
                        date
                    )
                    binding.spnEDate.adapter = dateAdapter
                } else if (month[p2].equals("2")) {
                    val date = mutableListOf<String>()

                    for (i in 1..28) {
                        date.add("${i}")
                    }

                    var dateAdapter = ArrayAdapter<String>(
                        requireContext(),
                        android.R.layout.simple_list_item_1,
                        date
                    )
                    binding.spnEDate.adapter = dateAdapter
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }

        binding.btnBoarderEnter.setOnClickListener {
            Log.d("??? ???", "${id}")
            Log.d("??? ???", "${nick}")
            Log.d("??? ???", "${binding.btnBoarderSpinner.selectedItem}")
            Log.d("??? ???", "${binding.btnBoarderTitle.text}")
            Log.d("??? ???", "${binding.spnSYear.selectedItem}-${binding.spnSMonth.selectedItem}-${binding.spnSDate.selectedItem}")
            Log.d("??? ???", "${binding.spnEYear.selectedItem}-${binding.spnEMonth.selectedItem}-${binding.spnEDate.selectedItem}")
            Log.d("??? ???", "${binding.editTextTextMultiLine.text}")
            Log.d("??? ???", "${via1X}")
            Log.d("??? ???", "${via1Y}")
            Log.d("??? ???", "${via1Name}")
            Log.d("??? ???", "${via2X}")
            Log.d("??? ???", "${via2Y}")
            Log.d("??? ???", "${img1Name}")
            Log.d("??? ???", "${img2Name}")
            Log.d("??? ???", "${img3Name}")



            var gson = GsonBuilder().setLenient().create()

            var retrofit = Retrofit.Builder()
                .baseUrl("http://junni802.cafe24.com")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .client(OkHttpClient())
                .build()

            var write = retrofit.create(BoarderWriteService::class.java)

            write.requestBoarderWrite("${id}", "${nick}", "${binding.btnBoarderSpinner.selectedItem}", "${binding.btnBoarderTitle.text}", "${binding.spnSYear.selectedItem}-${binding.spnSMonth.selectedItem}-${binding.spnSDate.selectedItem}", "${binding.spnEYear.selectedItem}-${binding.spnEMonth.selectedItem}-${binding.spnEDate.selectedItem}",
            "${binding.editTextTextMultiLine.text}", "${via1X}", "${via1Y}", "${via1Name}", "${via2X}", "${via2Y}", "${via2Name}", "${via3X}", "${via3Y}", "${via3Name}", "${img1Name}", "${img2Name}", "${img3Name}").enqueue(object :
                Callback<BoarderWriteData> {
                override fun onResponse(
                    call: Call<BoarderWriteData>,
                    response: Response<BoarderWriteData>
                ) {
                    // ????????? ??????????????? ???????????? ?????????
                    Log.d("??????write ???", "${response.body()!!.num}")
                }

                override fun onFailure(call: Call<BoarderWriteData>, t: Throwable) {

                }

            })
        }

        return binding.root
    }

    fun convertBitmapToFile(bitmap: Bitmap): File {
        val newFile = File(activity?.applicationContext?.filesDir, "picture")
        val out = FileOutputStream(newFile)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, out)
        return newFile
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // ???????????? resultCode??? ???????????? ??????
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                1234 -> {
                    via1X = data?.getStringExtra("x").toString()
                    via1Y = data?.getStringExtra("y").toString()
                    via1Name = data?.getStringExtra("name").toString()
                    Log.d("x ???", "${via1X}")
                    Log.d("y ???", "${via1Y}")
                    Log.d("name ???", "${via1Name}")
                    binding.btnMap1.setText(via1Name)
                }
                12345 -> {
                    via2X = data?.getStringExtra("x").toString()
                    via2Y = data?.getStringExtra("y").toString()
                    via2Name = data?.getStringExtra("name").toString()
                    binding.btnMap2.setText(via2Name)
                }
                123456 -> {
                    via3X = data?.getStringExtra("x").toString()
                    via3Y = data?.getStringExtra("y").toString()
                    via3Name = data?.getStringExtra("name").toString()
                    binding.btnMap3.setText(via3Name)
                }
                CHOOSE_PICTURE1 -> {
                    try {
                        val input = data?.data?.let { requireActivity().contentResolver.openInputStream(it) }
                        val img: Bitmap = BitmapFactory.decodeStream(input)
                        input?.close()
                        bitmap = img
                        Log.d("?????? ???", "${img}")
                        binding.btnImage1.setImageBitmap(img)
                        var file = convertBitmapToFile(bitmap!!)
                        if (bitmap != null) {
//                println("token = ${Api.authToken}")
                            var date = SimpleDateFormat("yyyyMMdd_hh:mm:ss").format(Date())
                            img1Name = "${id}${date}.jpg"
                            file = convertBitmapToFile(bitmap!!)
                            var requestBody = RequestBody.create(MediaType.parse("image/*"),file)
                            val survey = RequestBody.create(MediaType.parse("image/*"), file)
                            var body = MultipartBody.Part.createFormData("file", "${img1Name}", survey)
                            var gson = GsonBuilder().setLenient().create()

                            var retrofit = Retrofit.Builder()
                                .baseUrl("http://junni802.cafe24.com")
                                .addConverterFactory(GsonConverterFactory.create(gson))
                                .client(OkHttpClient())
                                .build()

                            var img12 = retrofit.create(ImageService::class.java)

                            img12.sendFile(body).enqueue(object :
                                Callback<String> {
                                override fun onResponse(
                                    call: Call<String>,
                                    response: Response<String>
                                ) {
                                    Log.d("???????????? ???", "?????? ??????")
                                }

                                override fun onFailure(call: Call<String>, t: Throwable) {
                                    Log.d("???????????? ???", "?????? ??????")
                                }

                            })


                        } else { }
                    } catch (e: Exception) {
                        println("????????? ???????????? ??????")
                    }
                }
                CHOOSE_PICTURE2 -> {
                    try {
                        val input = data?.data?.let { requireActivity().contentResolver.openInputStream(it) }
                        val img: Bitmap = BitmapFactory.decodeStream(input)
                        input?.close()
                        bitmap = img
                        Log.d("?????? ???", "${img}")
                        binding.btnImage2.setImageBitmap(img)
                        var file = convertBitmapToFile(bitmap!!)

                        if (bitmap != null) {
//                println("token = ${Api.authToken}")
                            var date = SimpleDateFormat("yyyyMMdd_hh:mm:ss").format(Date())
                            img2Name = "${id}${date}.jpg"
                            file = convertBitmapToFile(bitmap!!)
                            var requestBody = RequestBody.create(MediaType.parse("image/*"),file)
                            val survey = RequestBody.create(MediaType.parse("image/*"), file)
                            var body = MultipartBody.Part.createFormData("file", "${img2Name}", survey)
                            var gson = GsonBuilder().setLenient().create()

                            var retrofit = Retrofit.Builder()
                                .baseUrl("http://junni802.cafe24.com")
                                .addConverterFactory(GsonConverterFactory.create(gson))
                                .client(OkHttpClient())
                                .build()

                            var img12 = retrofit.create(ImageService::class.java)

                            img12.sendFile(body).enqueue(object :
                                Callback<String> {
                                override fun onResponse(
                                    call: Call<String>,
                                    response: Response<String>
                                ) {
                                    Log.d("???????????? ???", "?????? ??????")
                                }

                                override fun onFailure(call: Call<String>, t: Throwable) {
                                    Log.d("???????????? ???", "?????? ??????")
                                }

                            })


                        } else { }
                    } catch (e: Exception) {
                        println("????????? ???????????? ??????")
                    }
                }
                CHOOSE_PICTURE3 -> {
                    try {
                        val input = data?.data?.let { requireActivity().contentResolver.openInputStream(it) }
                        val img: Bitmap = BitmapFactory.decodeStream(input)
                        input?.close()
                        bitmap = img
                        Log.d("?????? ???", "${img}")
                        binding.btnImage3.setImageBitmap(img)
                        var file = convertBitmapToFile(bitmap!!)


                        if (bitmap != null) {
//                println("token = ${Api.authToken}")
                            var date = SimpleDateFormat("yyyyMMdd_hh:mm:ss").format(Date())
                            img3Name = "${id}${date}.jpg"
                            file = convertBitmapToFile(bitmap!!)
                            var requestBody = RequestBody.create(MediaType.parse("image/*"),file)
                            val survey = RequestBody.create(MediaType.parse("image/*"), file)
                            var body = MultipartBody.Part.createFormData("file", "${img3Name}", survey)
                            var gson = GsonBuilder().setLenient().create()

                            var retrofit = Retrofit.Builder()
                                .baseUrl("http://junni802.cafe24.com")
                                .addConverterFactory(GsonConverterFactory.create(gson))
                                .client(OkHttpClient())
                                .build()

                            var img12 = retrofit.create(ImageService::class.java)

                            img12.sendFile(body).enqueue(object :
                                Callback<String> {
                                override fun onResponse(
                                    call: Call<String>,
                                    response: Response<String>
                                ) {
                                    Log.d("???????????? ???", "?????? ??????")
                                }

                                override fun onFailure(call: Call<String>, t: Throwable) {
                                    Log.d("???????????? ???", "?????? ??????")
                                }

                            })


                        } else { }
                    } catch (e: Exception) {
                        println("????????? ???????????? ??????")
                    }
                }
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
                // ????????? ??????????????? ???????????? ?????????
                var nickName = response.body()

                nick = nickName?.nick.toString()
                Log.d("????????? ???", "${nick}")
            }

            override fun onFailure(call: Call<ChattingNickData>, t: Throwable) {

            }

        })
    }


}
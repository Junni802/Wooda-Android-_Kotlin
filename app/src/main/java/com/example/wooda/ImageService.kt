package com.example.wooda

import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.http.*
import java.io.File
import java.io.FileInputStream
import java.io.OutputStream

interface ImageService {
    @Multipart
    @POST("/Image")
    fun sendFile(
        @Part file: MultipartBody.Part	// 이 부분이 우리가 넣을 데이터!!
    ): Call<String>

}
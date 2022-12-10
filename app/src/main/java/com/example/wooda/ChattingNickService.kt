package com.example.wooda

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ChattingNickService {
    @FormUrlEncoded
    @POST("/NickName")
    fun requestNickName(
        // 인풋을 정의하는곳
        @Field("email") email:String
    ) : Call<ChattingNickData>     // 아웃풋을 정의하는 곳
}
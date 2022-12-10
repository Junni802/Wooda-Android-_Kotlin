package com.example.wooda

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST

interface WoodaLoginServiceChk {
    @FormUrlEncoded
    @POST("/Login")
    @Headers("Content-Type:application/x-www-form-urlencoded; charset=utf-8")
    fun requestLogin(
        // 인풋을 정의하는곳
        @Field("uid") uid:String,
        @Field("upw") upw:String
    ) : Call<WoodaLoginData>     // 아웃풋을 정의하는 곳/

}
package com.example.wooda

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST

interface SendCoupleService {
    @FormUrlEncoded
    @POST("/CoupleSend")
    @Headers("Content-Type:application/x-www-form-urlencoded; charset=utf-8")
    fun requestSendCouple(
        @Field("sEmail") sEmail:String,
        @Field("rEmail") rEmail:String,
        @Field("endDate") endDate:String
    ) : Call<SendCoupleData>
}
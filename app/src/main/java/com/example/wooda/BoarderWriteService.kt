package com.example.wooda

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface BoarderWriteService {
    @FormUrlEncoded
    @POST("/Write")
    fun requestBoarderWrite(
        // 인풋을 정의하는곳
        @Field("email") email:String,
        @Field("nick") nick:String,
        @Field("area") area:String,
        @Field("title") title:String,
        @Field("sdate") sdate:String,
        @Field("edate") edate:String,
        @Field("content") content:String,
        @Field("lat1") lat1:String,
        @Field("lng1") lng1:String,
        @Field("place1") place1:String,
        @Field("lat2") lat2:String,
        @Field("lng2") lng2:String,
        @Field("place2") place2:String,
        @Field("lat3") lat3:String,
        @Field("lng3") lng3:String,
        @Field("place3") place3:String,
        @Field("img1") img1:String,
        @Field("img2") img2:String,
        @Field("img3") img3:String
    ) : Call<BoarderWriteData>     // 아웃풋을 정의하는 곳
}
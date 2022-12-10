package com.example.wooda

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST

interface MemberJoinService {
    @FormUrlEncoded
    @POST("/Join")
    @Headers("Content-Type:application/x-www-form-urlencoded; charset=utf-8")
    fun requestMemberJoin(
        @Field("email") email:String,
        @Field("name") name:String,
        @Field("nick") nick:String,
        @Field("pw") pw:String,
        @Field("birth") birth:String
    ) : Call<MemberJoinData>
}
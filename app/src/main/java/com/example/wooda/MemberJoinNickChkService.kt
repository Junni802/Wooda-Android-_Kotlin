package com.example.wooda

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST

interface MemberJoinNickChkService {
    @FormUrlEncoded
    @POST("/chkNick")
    @Headers("Content-Type:application/x-www-form-urlencoded; charset=utf-8")
    fun requestNickChk(
        @Field("nick") nick:String
    ) : Call<MemberJoinNickChkData>
}
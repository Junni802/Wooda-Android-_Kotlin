package com.example.wooda

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST

interface JoinSendMailService {
    @FormUrlEncoded
    @POST("/Mail")
    @Headers("Content-Type:application/x-www-form-urlencoded; charset=utf-8")
    fun requestSendMail(
        @Field("receiver") receiver:String
    ) : Call<JoinSendMailData>
}
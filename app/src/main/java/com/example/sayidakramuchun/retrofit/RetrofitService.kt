package com.example.sayidakramuchun.retrofit

import com.example.sayidakramuchun.models.MyHelp
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface RetrofitService {

    @POST("yordam/")
    fun createUser(@Body reqUser: MyHelp): Call<MyHelp>
}
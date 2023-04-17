package com.example.sayidakramuchun.retrofit

import com.example.sayidakramuchun.retrofit.ApiCient.ApiCLinet.getRetrofit
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ApiCient {
    object ApiCLinet {

        const val BASE_URL = "https://medhelp23.pythonanywhere.com/"

        fun getRetrofit(): Retrofit {
            return Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
        }

    }

    val apiService = getRetrofit().create(RetrofitService::class.java)
}
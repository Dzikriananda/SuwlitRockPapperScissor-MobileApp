package com.dzikri.suwlitrockpaperscissor.data.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance{
    private const val BASE_URL ="http://43.173.1.87:8081/"

    fun getInstance(): Retrofit {
        val client = OkHttpClient()
        val interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        val clientBuilder: OkHttpClient.Builder = client.newBuilder().addInterceptor(interceptor as HttpLoggingInterceptor)
        val retrofit = Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
//            .client(clientBuilder.build())
            .client(okhttpClient())
            .build()
        return retrofit
    }

//    private fun okhttpClient(): OkHttpClient {
//        return OkHttpClient.Builder()
//            .addInterceptor(AuthInterceptor())
//            .build()
//    }

}
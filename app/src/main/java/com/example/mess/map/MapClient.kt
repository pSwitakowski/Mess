package com.example.mess.map


import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

class MapClient {
    fun getClient(): MapService {
        val httpClient = OkHttpClient.Builder()

        val builder = Retrofit.Builder()
            .baseUrl("http://10.0.2.2:4040/")
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())

        val retrofit = builder
            .client(httpClient.build())
            .build()

        return retrofit.create(MapService::class.java)
    }
}
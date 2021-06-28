package com.example.mess.map

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface MapService {
    @POST("location")
    fun sendLocation(@Body coordinates: okhttp3.RequestBody): Call<String>
}
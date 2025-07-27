package com.example.dserve

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {
    @POST("signup/user")
    suspend fun signupu(@Body request: UserSignupRequest): Response<SignupResponse>
    @POST("/signup/admin")
    suspend fun signup(@Body request: AdminSignupRequest): Response<SignupResponse>
    @POST("/login/admin")
    suspend fun loginAdmin(@Body request: AdminLoginRequest): Response<LoginResponse>

    @POST("/login/user")
    suspend fun loginUser(@Body request: UserLoginRequest): Response<LoginResponse>
    @POST("admin/add_stall")
    suspend fun addStall(@Body stall: StallRequest): Response<StallResponse>
    @GET("/get-stalls/")
        suspend fun getStalls(): List<StallRequest>
    }



object RetrofitInstance {

    val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("http://10.0.2.2:8000/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}
package com.example.dserve

import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

interface ApiService {
    @POST("signup/user")
    suspend fun signupu(@Body request: UserSignupRequest): Response<SignupResponse>
    @POST("signup/admin")
    suspend fun signup(@Body request: AdminSignupRequest): Response<SignupResponse>
    @POST("/login/admin")
    suspend fun loginAdmin(@Body request: AdminLoginRequest): Response<LoginResponse>

    @POST("/login/user")
    suspend fun loginUser(@Body request: UserLoginRequest): Response<LoginResponse>

}
object RetrofitInstance {

    val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl("http://<your-ip-or-domain>:8000/") // Replace with your actual base URL
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}
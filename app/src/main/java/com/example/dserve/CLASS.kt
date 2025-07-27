package com.example.dserve

data class UserSignupRequest(
    val username: String,
    val password: String
)
data class AdminSignupRequest(
    val adminId: String,
    val password: String,
)
data class AdminLoginRequest(
    val adminId: String,
    val password: String
)

data class LoginResponse(
    val message: String
)
data class UserLoginRequest(
    val username: String,
    val password: String
)

data class SignupResponse(
    val message: String
)
data class StallRequest(
    val name: String,
    val description: String,
    val menu: List<MenuItemRequest>
)
data class MenuItemRequest(
    val itemName: String,
    val price: Double
)
data class StallResponse(
    val status: String,
    val stall_id: Int
)

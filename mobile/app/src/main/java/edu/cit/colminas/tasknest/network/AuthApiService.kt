package edu.cit.colminas.tasknest.network

import edu.cit.colminas.tasknest.model.LoginRequest
import edu.cit.colminas.tasknest.model.LoginResponse
import edu.cit.colminas.tasknest.model.RegisterRequest
import edu.cit.colminas.tasknest.model.RegisterResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApiService {
    @POST("/api/auth/login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>
    
    @POST("/api/auth/register")
    suspend fun register(@Body request: RegisterRequest): Response<RegisterResponse>
}

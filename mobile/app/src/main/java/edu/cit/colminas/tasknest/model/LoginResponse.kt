package edu.cit.colminas.tasknest.model

data class LoginResponse(
    val success: Boolean,
    val data: LoginData?,
    val error: Any?
)

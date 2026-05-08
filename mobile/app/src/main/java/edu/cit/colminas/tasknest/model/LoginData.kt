package edu.cit.colminas.tasknest.model

data class LoginData(
    val userId: Long,
    val username: String,
    val firstName: String,
    val lastName: String,
    val token: String,
    val message: String
)

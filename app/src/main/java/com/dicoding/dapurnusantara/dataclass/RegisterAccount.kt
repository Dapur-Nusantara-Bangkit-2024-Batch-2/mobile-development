package com.dicoding.dapurnusantara.dataclass
data class RegisterDataAccount(
    var name: String,
    var email: String,
    var password: String
)

data class LoginDataAccount(
    var email: String,
    var password: String
)

data class ResponseLogin(
    var error: Boolean,
    var message: String,
    var loginResult: LoginResult
)

data class ResponseDetail(
    var error: Boolean,
    var message: String
)

data class LoginResult(
    var userId: String,
    var name: String,
    var token: String
)
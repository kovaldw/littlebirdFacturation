package com.wanoon.littlebirdFacturation.security.payload.requests

import javax.validation.constraints.NotBlank

class LoginRequest(
        @NotBlank
        var usernameOrEmail:String = "",

        @NotBlank
        var password:String = ""
)
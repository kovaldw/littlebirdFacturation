package com.wanoon.littlebirdFacturation.security.payload.requests

import javax.validation.constraints.Email
import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size


class SignupRequest(
        @NotBlank
        @Size(min = 3, max = 15)
        var name:String = "",

        @NotBlank
        @Size(min = 3, max = 15)
        var username:String = "",

        @NotBlank
        @Size(max = 40)
        @Email
        var email:String = "",

        @NotBlank
        @Size(min = 6, max = 20)
        var password:String = ""

)
{
}
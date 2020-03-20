package com.wanoon.littlebirdFacturation.security.payload.responses


class JwtAuthenticationResponse (
        var accessToken:String = "",
        var tokenType:String = "Bearer"
) {
}
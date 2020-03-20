package com.wanoon.payload.responses

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)

class ApiResponse(
        var success:Boolean = false,
        var message:String = "",
        var result:Any? = null
) {
}
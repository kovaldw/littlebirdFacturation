package com.wanoon.littlebirdFacturation.exceptions

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import java.lang.RuntimeException

@ResponseStatus(HttpStatus.BAD_REQUEST)
class BadRequestException (
        override var message:String = "",
        override val cause: Throwable? = null
): RuntimeException() {
}
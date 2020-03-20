package com.wanoon.littlebirdFacturation.exceptions

import java.lang.RuntimeException

class AppException (
        override var message:String = "",
        override var cause:Throwable? = null

        ): RuntimeException()
{

}
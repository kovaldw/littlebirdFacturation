package com.wanoon.littlebirdFacturation

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import springfox.documentation.swagger2.annotations.EnableSwagger2

@SpringBootApplication
class LittlebirdFacturationApplication

fun main(args: Array<String>) {
	runApplication<LittlebirdFacturationApplication>(*args)
}

package com.wanoon.littlebirdFacturation.controller

import com.wanoon.littlebirdFacturation.repository.AnnexeRepository
import com.wanoon.payload.responses.ApiResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController


@RestController
@RequestMapping("/api/annexe")
class AnnexeController
{

    @Autowired
    lateinit var annexeRepository:AnnexeRepository

    @GetMapping("/")
    fun list():ResponseEntity<Any>
    {
        return ResponseEntity.ok(ApiResponse(true, "ok", annexeRepository.findAll()))
    }

    fun create()
    {

    }


}
package com.wanoon.littlebirdFacturation.controller

import com.wanoon.littlebirdFacturation.model.Societe
import com.wanoon.littlebirdFacturation.payload.requests.societe.NewSocieteRequest
import com.wanoon.littlebirdFacturation.repository.SocieteRepository
import com.wanoon.littlebirdFacturation.repository.SocieteSpecifications
import com.wanoon.littlebirdFacturation.security.authentication.UserPrincipal
import com.wanoon.littlebirdFacturation.security.model.User
import com.wanoon.littlebirdFacturation.security.services.UserServices
import com.wanoon.littlebirdFacturation.services.ApiServices
import com.wanoon.littlebirdFacturation.services.SocieteServices
import com.wanoon.payload.responses.ApiResponse
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification.where
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.servlet.http.HttpServletRequest
import kotlin.collections.ArrayList


@RestController
@RequestMapping("/api/societe")
@Api(description = "Ce controller gere les societes")
class SocieteController
{

    @Autowired
    lateinit var apiServices:ApiServices


    @Autowired
    lateinit var userServices: UserServices

    @Autowired
    lateinit var societeRepository:SocieteRepository

    @Autowired
    lateinit var societeServices: SocieteServices

    @ApiOperation(value = "Récupère la liste des societés", notes = "Permet de récupérer toute la liste des sociétés")
    @GetMapping("/")
    fun list(request:HttpServletRequest):ResponseEntity<Any>
    {
        try
        {
            val parameters = apiServices.getParametersFromGetRequest(request)

            var numeroPage:Int = apiServices.getNumeroPageFromParameters(parameters)
            var nombreElements:Int = apiServices.getNbElementsFromParameters(parameters)

            var pageable: Pageable = PageRequest.of(numeroPage, nombreElements)

            var societes = societeRepository.findAll(SocieteSpecifications.isNotDeleted(), pageable)

            return ResponseEntity.ok(societes)
        }
        catch (e:Exception)
        {
            return ResponseEntity.ok(ApiResponse(true, e.message.toString()))
        }
    }


    @ApiOperation(value = "Instance d'une societe", notes = "Recuperer l'instance d'une société par son ID")
    @GetMapping("/{id}")
    fun one(
            @ApiParam(required = true, name = "id", value = "ID de la societe à récupérer", defaultValue = "0")
            @PathVariable id:Long):ResponseEntity<Any>
    {

        try {
//            var societe = societeRepository.findByIdOrNull(id)
            var societe = societeRepository.findOneByIdAndDeletedFalse(id)

            if (societe == null)
            {
                return ResponseEntity.ok(ApiResponse(message = "Societe introuvable"))
            }

            return ResponseEntity.ok(societe)

        }
        catch (e:Exception)
        {
            return ResponseEntity.ok(ApiResponse(false, e.message.toString()))
        }

    }


    @ApiOperation(value = "Suppression d'une societe", notes = "Supprimer une societe par son ID")
    @DeleteMapping("/{id}")
    fun delete(
            @ApiParam(value = "ID de la société à supprimer", name = "id", defaultValue = "0", required = true)
            @PathVariable id:Long
    ):ResponseEntity<Any>
    {
        try
        {
            var societe:Societe? = societeRepository.findOneByIdAndDeletedFalse(id)

            if (societe == null)
            {
                return ResponseEntity.ok(ApiResponse(false, "La societe a suppprimer nexiste pas"));
            }

            var user: User? = userServices.getCurrentUser()
            societe.deleted = true
            societe.deletedAt = Date()
            societe.deletedBy = user

            societeRepository.save(societe)

//            societeRepository.delete(societe)

            return ResponseEntity.ok(ApiResponse(true, "Societe supprimee avec succes"))

        }
        catch (e:Exception)
        {
            return ResponseEntity.ok(ApiResponse(message = e.message.toString()))
        }

    }


    @ApiOperation(value = "Enregistrement d'une nouvelle societe")
    @PostMapping("/")
    fun new(@RequestBody @ApiParam(value = "Instance de la societe", name = "societe", required = true) societeRequest:NewSocieteRequest):ResponseEntity<Any>
    {
        try {

            val validationRequest = societeServices.validerSociete(societeRequest)
            if (validationRequest["valid"] == false)
            {
                return ResponseEntity.ok(ApiResponse(message = validationRequest["message"].toString()))
            }

            if (societeRepository.existsByRegistrationNumberAndDeletedFalse(societeRequest.registrationNumber))
            {
                return ResponseEntity.ok(ApiResponse(message = "Une societe du meme numero de registre existe deja"))
            }

            var societe = Societe()
            societe.address = societeRequest.address.trim()
            societe.favoriteNumber = societeRequest.favoriteNumber.trim()
            societe.name = societeRequest.name.trim()
            societe.registrationNumber = societeRequest.registrationNumber.trim()
            societe.termPayment = societeRequest.termPayment
            societe.type = societeRequest.type
            societe.url = societeRequest.url

            var user: User? = userServices.getCurrentUser()
            societe.createdBy = user

            societeRepository.save(societe)
            return ResponseEntity.ok(ApiResponse(true, "Societe enregistree avec success", societe.id))

        }
        catch (e:Exception)
        {
            return ResponseEntity.ok(ApiResponse(message = e.message.toString()))
        }
    }

    @ApiOperation(value = "Modifier une societe existante")
    @PutMapping("/{id}")
    fun edit(@PathVariable @ApiParam(value = "Id de la societe a modifier", name = "id", required = true) id:Long,
             @RequestBody @ApiParam(value = "Instance de la nouvelle societe", name = "societe", required = true
             ) societeRequest: NewSocieteRequest):ResponseEntity<Any>
    {
        try {

            var societe = societeRepository.findOneByIdAndDeletedFalse(id)
            if (societe == null)
            {
                return ResponseEntity.ok(ApiResponse(message = "Societe à modifier introuvable"))
            }

            val validationRequest = societeServices.validerSociete(societeRequest)
            if (validationRequest["valid"] == false)
            {
                return ResponseEntity.ok(ApiResponse(message = validationRequest["message"].toString()))
            }

            var societeExiste = societeRepository.findOneByRegistrationNumberAndDeletedFalse(societeRequest.registrationNumber)
            if (societeExiste != null && societeExiste.registrationNumber !== societe.registrationNumber)
            {
                return ResponseEntity.ok(ApiResponse(message = "Une societe du meme numero de registre existe deja"))
            }

            societe.address = societeRequest.address.trim()
            societe.favoriteNumber = societeRequest.address.trim()
            societe.name = societeRequest.name.trim()
            societe.registrationNumber = societeRequest.registrationNumber.trim()
            societe.termPayment = societeRequest.termPayment
            societe.type = societeRequest.type.trim()
            societe.url = societeRequest.url.trim()

            var user = userServices.getCurrentUser()

            societe.updatedAt = Date()
            societe.updatedBy = user

            societeRepository.save(societe)

            return ResponseEntity.ok(societe)

        }
        catch (e:Exception)
        {
            return ResponseEntity.ok(ApiResponse(message = e.message.toString()))
        }

    }

}
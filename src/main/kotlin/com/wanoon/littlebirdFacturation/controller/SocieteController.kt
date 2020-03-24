package com.wanoon.littlebirdFacturation.controller

import com.sun.org.apache.xpath.internal.operations.Bool
import com.wanoon.littlebirdFacturation.model.Societe
import com.wanoon.littlebirdFacturation.repository.SocieteRepository
import com.wanoon.littlebirdFacturation.services.SocieteServices
import com.wanoon.payload.responses.ApiResponse
import io.swagger.annotations.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.ResponseEntity
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
    lateinit var societeRepository:SocieteRepository

    @Autowired
    lateinit var societeServices: SocieteServices

    @ApiOperation(value = "Récupère la liste des societés", notes = "Permet de récupérer toute la liste des sociétés")
    @GetMapping("/")
    fun list():ResponseEntity<Any>
    {
        var societes:MutableList<Societe> = ArrayList()
        try
        {
            societes = societeRepository.findAll()
            return ResponseEntity.ok(ApiResponse(true, "OK", societes))
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
            var societe = societeRepository.findByIdOrNull(id)

            if (societe == null)
            {
                return ResponseEntity.ok(ApiResponse(message = "Societe introuvable"))
            }

            return ResponseEntity.ok(ApiResponse(true, "OK", societe))

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
            var societe:Societe? = societeRepository.findByIdOrNull(id)

            if (societe == null)
            {
                return ResponseEntity.ok(ApiResponse(false, "La societe a suppprimer nexiste pas"));
            }

            societeRepository.delete(societe)

            return ResponseEntity.ok(ApiResponse(true, "Societe supprimee avec succes"))

        }
        catch (e:Exception)
        {
            return ResponseEntity.ok(ApiResponse(message = e.message.toString()))
        }

    }


    @ApiOperation(value = "Enregistrement d'une nouvelle societe")
    @PostMapping("/")
    fun new(@RequestBody @ApiParam(value = "Instance de la societe", name = "societe", required = true) societe:Societe):ResponseEntity<Any>
    {
        try {
            if (!societeServices.validerSociete(societe))
            {
                return ResponseEntity.ok(ApiResponse(message = "Veuillez vérifier les informations envoyees"))
            }

            if (societeRepository.existsByRegistrationNumber(societe.registrationNumber))
            {
                return ResponseEntity.ok(ApiResponse(message = "Une societe du meme numero de registre existe deja"))
            }

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
             ) societe:Societe):ResponseEntity<Any>
    {
        try {

            var oldSociete = societeRepository.findByIdOrNull(id)
            if (oldSociete == null)
            {
                return ResponseEntity.ok(ApiResponse(message = "Societe à modifier introuvable"))
            }

            if (!societeServices.validerSociete(societe))
            {
                return ResponseEntity.ok(ApiResponse(message = "Veuillez vérifier les informations envoyees"))
            }

            var societeExiste = societeRepository.findOneByRegistrationNumber(societe.registrationNumber)
            if (societeExiste != null && societeExiste.registrationNumber !== oldSociete.registrationNumber)
            {
                return ResponseEntity.ok(ApiResponse(message = "Une societe du meme numero de registre existe deja"))
            }

            var newSociete = societeServices.setSociete(oldSociete, societe)

            societeRepository.save(newSociete)
            return ResponseEntity.ok(ApiResponse(true, "Societe modifiee avec success", societe.id))

        }
        catch (e:Exception)
        {
            return ResponseEntity.ok(ApiResponse(message = e.message.toString()))
        }

    }

}
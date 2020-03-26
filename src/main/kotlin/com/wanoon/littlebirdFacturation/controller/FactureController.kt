package com.wanoon.littlebirdFacturation.controller

import com.wanoon.littlebirdFacturation.model.Facture
import com.wanoon.littlebirdFacturation.model.LigneFacturation
import com.wanoon.littlebirdFacturation.payload.requests.facture.NewFactureRequest
import com.wanoon.littlebirdFacturation.repository.FactureRepository
import com.wanoon.littlebirdFacturation.repository.FactureSpecifications
import com.wanoon.littlebirdFacturation.security.services.UserServices
import com.wanoon.littlebirdFacturation.services.ApiServices
import com.wanoon.littlebirdFacturation.services.FactureServices
import com.wanoon.payload.responses.ApiResponse
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.math.BigDecimal
import java.util.*
import javax.servlet.http.HttpServletRequest
import kotlin.collections.ArrayList


@RestController
@RequestMapping("/api/facture")
@Api(value = "Controller qui gere le CRUD de facture")
class FactureController
{

    @Autowired
    lateinit var apiServices: ApiServices

    @Autowired
    lateinit var userServices: UserServices

    @Autowired
    lateinit var factureRepository: FactureRepository

    @Autowired
    lateinit var factureServices: FactureServices

    var logger:Logger = LoggerFactory.getLogger(FactureController::class.java)



    @ApiOperation(value = "Liste des factures", notes = "Permet de récupérer la liste des factures")
    @GetMapping("/")
    fun list(request:HttpServletRequest): ResponseEntity<Any>
    {
        try
        {
            var parameters = apiServices.getParametersFromGetRequest(request)

            var numeroPage = apiServices.getNumeroPageFromParameters(parameters)
            var nbElements = apiServices.getNbElementsFromParameters(parameters)

            var pageable = PageRequest.of(numeroPage, nbElements)

            var factures = factureRepository.findAll(FactureSpecifications.isNotDeleted(), pageable)

            return ResponseEntity.ok(factures)

        }
        catch (e:Exception)
        {
            return ResponseEntity.ok(ApiResponse(message = "Erreur: ${e.message.toString()}"))
        }

    }


    @ApiOperation(value = "Recupere une facture", notes = "Recuperer l'instance d'une facture par son ID")
    @GetMapping("/{id}")
    fun one(
            @ApiParam(value = "Id de la facture à récupérer", name = "id", required = true)
            @PathVariable id:Long): ResponseEntity<Any>
    {
        try
        {
            var facture: Facture? = factureRepository.findOneByIdAndDeletedFalse(id)
                    ?: return ResponseEntity.ok(ApiResponse(message = "Facture introuvable"))

            return ResponseEntity.ok(facture!!)

        }
        catch (e:Exception)
        {
            return ResponseEntity.ok(ApiResponse(message = "Erreur: ${e.message.toString()}"))
        }
    }



    @ApiOperation(value = "Supprimer une facture", notes = "Permet de supprimer uen facture grace a son ID")
    @DeleteMapping("/{id}")
    fun delete(
            @ApiParam(value = "ID de la facture à supprimer", name = "id", required = true)
            @PathVariable id:Long): ResponseEntity<Any>
    {

        try
        {
            var facture: Facture? = factureRepository.findOneByIdAndDeletedFalse(id)
                    ?: return ResponseEntity.ok(ApiResponse(message = "Facture à supprimer introuvable"))

            val user = userServices.getCurrentUser()

            facture!!.deleted = false
            facture.deletedAt = Date()
            facture.deletedBy = user

            factureRepository.save(facture)

            return ResponseEntity.ok(ApiResponse(success = true, message = "Facture supprimee avec success"))
        }
        catch (e:Exception)
        {
            return ResponseEntity.ok(ApiResponse(message = "Erreur: ${e.message.toString()}"))
        }

    }


    @ApiOperation(value = "Enregistrement d'une facture", notes = "Permet de faire une nouvelle facturation")
    @PostMapping("/")
    fun create(
            @ApiParam(value = "Instance de la facture à enregistrer", name = "facture", required = true)
            @RequestBody
            factureRequest: NewFactureRequest?
    ): ResponseEntity<Any>
    {
        try
        {
            if (factureRequest == null)
            {
                return ResponseEntity.ok(ApiResponse(message = "Aucune requete envoyee. Veuillez réésayer"))
            }

            val validationRequest = factureServices.validerNewFactureRequest(factureRequest)
            if (validationRequest["valid"] == false)
            {
                return ResponseEntity.ok(ApiResponse(message = validationRequest["message"].toString()))
            }

            if (factureRepository.existsByRefAndDeletedFalse(factureRequest.ref.trim()))
            {
                return ResponseEntity.ok(ApiResponse(message = "Une facture du meme reference existe deja"))
            }

            var lignesFacturationsIds = factureRequest.lignesFacturations

            var lignesFacturations:ArrayList<LigneFacturation> = factureServices.recupererFacturationsFromIds(lignesFacturationsIds)

            val montant = factureServices.calculerMontantFacture(lignesFacturations)
            if (montant == BigDecimal(0))
            {
                return ResponseEntity.ok(ApiResponse(message = "Le montant de la facture est egal à 0"))
            }
            val montantTTC = factureServices.calculerMontantFactureTTC(lignesFacturations)
            if (montantTTC == BigDecimal(0))
            {
                return ResponseEntity.ok(ApiResponse(message = "Le montant TTC est egal à 0"))
            }

            var facture = Facture()

            facture.ref = factureRequest.ref.trim()
            facture.billingAddress = factureRequest.billingAddress.trim()
            facture.filePath = factureRequest.filePath.trim()
            facture.type = factureRequest.type.trim()
            facture.amount = montant
            facture.amountTTC = montantTTC

            val user = userServices.getCurrentUser()
            facture.createdBy = user

            factureRepository.save(facture)

            factureServices.setFactureForLigneFacturation(lignesFacturations, facture)

            return ResponseEntity.ok(ApiResponse(success = true, message = "Facture enregistree avec success", response = facture.id))

        }
        catch (e:Exception)
        {
            logger.error(e.toString())
            return ResponseEntity.ok("Erreur: ${e.message.toString()}")
        }



    }




}
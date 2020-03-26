package com.wanoon.littlebirdFacturation.controller

import com.wanoon.littlebirdFacturation.model.Facture
import com.wanoon.littlebirdFacturation.payload.requests.facture.NewLigneFacturationRequest
//import com.wanoon.littlebirdFacturation.payload.requests.facture.NewListeLignesFacturationsRequest
import com.wanoon.littlebirdFacturation.repository.FactureRepository
import com.wanoon.littlebirdFacturation.repository.LigneFacturationRepository
import com.wanoon.littlebirdFacturation.repository.LigneFacturationSpecifications
import com.wanoon.littlebirdFacturation.security.services.UserServices
import com.wanoon.littlebirdFacturation.services.ApiServices
import com.wanoon.littlebirdFacturation.services.LigneFacturationServices
import com.wanoon.payload.responses.ApiResponse
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest


@RestController
@Api("Controller qui gere les lignes de facturation")
@RequestMapping("/api/ligne/facturation")
class LigneFacturationController
{

    @Autowired
    lateinit var apiServices: ApiServices

    @Autowired
    lateinit var userServices: UserServices

    @Autowired
    lateinit var ligneFacturationServices: LigneFacturationServices

    @Autowired
    lateinit var ligneFacturationRepository: LigneFacturationRepository

    @Autowired
    lateinit var factureRepository: FactureRepository


    @ApiOperation(value = "Liste des lignes de facturation", notes = "Permet de récupérer la liste des lignes de facturation. On peut filtrer par facture,colis")
    @GetMapping("/")
    fun list(reqquest:HttpServletRequest): ResponseEntity<Any>
    {


        try {
            var parameters = apiServices.getParametersFromGetRequest(reqquest)

            var numeroPage = apiServices.getNumeroPageFromParameters(parameters)
            var nbElements = apiServices.getNbElementsFromParameters(parameters)

            var factureId:Long? = null; var facture: Facture? = null; var factureFromRequest = false
            if (parameters.containsKey("facture"))
            {
                factureId =parameters["facture"].toString().toLong()
                facture = factureRepository.findOneByIdAndDeletedFalse(factureId)
                factureFromRequest = true
            }

            var colisId: Long? = null; var colisFromRequest = false
            if (parameters.containsKey("colis"))
            {
                colisId = parameters["colis"].toString().toLong()
                colisFromRequest = true
            }

            var pageable = PageRequest.of(numeroPage, nbElements)
            var lignesFacturations = ligneFacturationRepository.findAll(Specification.where(
                    LigneFacturationSpecifications.isNotDeleted()
                            .and(LigneFacturationSpecifications.withFacture(facture, factureFromRequest))
                            ?.and(LigneFacturationSpecifications.withColis(colisId, colisFromRequest))
            ), pageable)


            return ResponseEntity.ok(lignesFacturations)

        }
        catch (e:Exception)
        {
            return ResponseEntity.ok(ApiResponse(message = "Erreur: ${e.message.toString()}"))
        }

    }


    @GetMapping("/{id}")
    @ApiOperation(value = "Permet de récupérer une ligne de facturation", notes = "Permet de récupération l'iinstance d'une ligne de facturation grace a son ID")
    fun one(
            @ApiParam(value = "ID de la ligne à récupérer", name = "id", required = true)
            @PathVariable
            id:Long): ResponseEntity<Any>
    {
        try
        {
            val ligneFacturation = ligneFacturationRepository.findOneByIdAndDeletedFalse(id)
            if (ligneFacturation == null)
            {
                return ResponseEntity.ok(ApiResponse(message = "Ligne de facturation introuvable"))
            }

            return ResponseEntity.ok(ligneFacturation)

        }
        catch (e:Exception)
        {
            return ResponseEntity.ok(ApiResponse(message = "Erreur: ${e.message.toString()}"))
        }
    }


    @PostMapping("/")
    @ApiOperation(value = "Enregistrer une ligne de facturation", notes = "Permet d'enregistrer une nouvelle ligne de facturation")
    fun create(
            @ApiParam(value = "Instance de ligne de facturation", name = "ligneFacturation", required = true)
            @RequestBody
            lignesFacturationsRequest: ArrayList<NewLigneFacturationRequest?>?
    ):ResponseEntity<Any>
    {
        try
        {
            if (lignesFacturationsRequest == null)
            {
                return ResponseEntity.ok(ApiResponse(message = "Aucune requete envoyee. Veuillez réésayer"))
            }

            val resultatValidation = ligneFacturationServices.validerListeNewLignesFacturations(lignesFacturationsRequest)
            if (resultatValidation["valid"] == false)
            {
                return ResponseEntity.ok(ApiResponse(message = resultatValidation["message"].toString()))
            }

            val lignesFacturations = ligneFacturationServices.formatterLignesFacturationsPourEnregistrement(lignesFacturationsRequest)

            if (lignesFacturations.size.compareTo(lignesFacturationsRequest.size) != 0)
            {
                return ResponseEntity.ok(ApiResponse(message = "Une erreur incounnue lors du traitement des lignes de facturation"))
            }

            var nbEnregistrer = 0
            for (ligneFacturation in lignesFacturations)
            {
                ligneFacturationRepository.save(ligneFacturation)
                nbEnregistrer++
            }

            return ResponseEntity.ok(ApiResponse(success = true, message = "$nbEnregistrer ligne(s) enregistrée(s) sur ${lignesFacturations.size} au total"))

        }
        catch (e:Exception)
        {
            return ResponseEntity.ok(ApiResponse(message = "Erreur: ${e.message.toString()}"))
        }

    }

}
package com.wanoon.littlebirdFacturation.controller

import NewAnnexeRequest
import com.wanoon.littlebirdFacturation.model.Annexe
import com.wanoon.littlebirdFacturation.model.Societe
import com.wanoon.littlebirdFacturation.repository.AnnexeRepository
import com.wanoon.littlebirdFacturation.repository.AnnexeSpecifications
import com.wanoon.littlebirdFacturation.repository.SocieteRepository
import com.wanoon.littlebirdFacturation.security.model.User
import com.wanoon.littlebirdFacturation.security.services.UserServices
import com.wanoon.littlebirdFacturation.services.AnnexeServices
import com.wanoon.littlebirdFacturation.services.ApiServices
import com.wanoon.littlebirdFacturation.services.SocieteServices
import com.wanoon.payload.responses.ApiResponse
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.domain.Specification
import org.springframework.data.jpa.domain.Specification.where
import org.springframework.data.repository.findByIdOrNull
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.servlet.http.HttpServletRequest
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


@RestController
@RequestMapping("/api/annexe")
@Api(description = "Ce controller gere les annexes des societes")
class AnnexeController
{

    @Autowired
    lateinit var apiServices: ApiServices

    @Autowired
    lateinit var userServices:UserServices

    @Autowired
    lateinit var societeRepository: SocieteRepository

    @Autowired
    lateinit var annexeRepository: AnnexeRepository

    @Autowired
    lateinit var societeServices: SocieteServices

    @Autowired
    lateinit var annexeServices: AnnexeServices

    var logger:Logger = LoggerFactory.getLogger(AnnexeController::class.java)

    @ApiOperation(value = "Récupère la liste des annexes", notes = "Permet de récupérer toute la liste des annexes")
    @GetMapping("/")
    fun list(request: HttpServletRequest):ResponseEntity<Any>
    {

        try
        {
            val parameters = apiServices.getParametersFromGetRequest(request)

            var numeroPage:Int = apiServices.getNumeroPageFromParameters(parameters)
            var nombreElements:Int = apiServices.getNbElementsFromParameters(parameters)

            var pageable:Pageable = PageRequest.of(numeroPage, nombreElements)

            var annexes = annexeRepository.findAll(AnnexeSpecifications.isNotDeleted(), pageable)

            return ResponseEntity.ok(annexes)
        }
        catch (e:Exception)
        {
            return ResponseEntity.ok(ApiResponse(true, e.message.toString()))
        }
    }


    @ApiOperation(value = "Instance d'une annexe", notes = "Recuperer l'instance d'une annexe par son ID")
    @GetMapping("/{id}")
    fun one(
            @ApiParam(required = true, name = "id", value = "ID de l'annexe à récupérer", defaultValue = "0")
            @PathVariable id:Long):ResponseEntity<Any>
    {

        try {
            var annexe = annexeRepository.findOneByIdAndDeletedFalse(id)

            if (annexe == null)
            {
                return ResponseEntity.ok(ApiResponse(message = "Annexe introuvable"))
            }

            return ResponseEntity.ok(annexe)

        }
        catch (e:Exception)
        {
            return ResponseEntity.ok(ApiResponse(false, e.message.toString()))
        }

    }


    @ApiOperation(value = "Suppression d'une annexe", notes = "Supprimer une annexe par son ID")
    @DeleteMapping("/{id}")
    fun delete(
            @ApiParam(value = "ID de l'annexe à supprimer", name = "id", defaultValue = "0", required = true)
            @PathVariable id:Long
    ):ResponseEntity<Any>
    {
        try
        {
            var annexe: Annexe? = annexeRepository.findOneByIdAndDeletedFalse(id)

            if (annexe == null)
            {
                return ResponseEntity.ok(ApiResponse(false, "L'annexe a suppprimer nexiste pas"));
            }

            var user: User? = userServices.getCurrentUser()
            annexe.deleted = true
            annexe.deletedAt = Date()
            annexe.deletedBy = user

            annexeRepository.save(annexe)

//            annexeRepository.delete(annexe)

            return ResponseEntity.ok(ApiResponse(true, "Annexe supprimee avec succes"))

        }
        catch (e:Exception)
        {
            return ResponseEntity.ok(ApiResponse(message = e.message.toString()))
        }

    }


    @ApiOperation(value = "Enregistrement d'une nouvelle annexe")
    @PostMapping("/")
    fun new(@RequestBody @ApiParam(value = "Instance de l'annexe", name = "annexe", required = true) annexeRequest: NewAnnexeRequest):ResponseEntity<Any>
    {
        try
        {
            val validationRequest = annexeServices.validerAnnexeRequest(annexeRequest)
            if (validationRequest["valid"] == false)
            {
                return ResponseEntity.ok(ApiResponse(message = validationRequest["message"].toString()))
            }
            var societeId = annexeRequest.societeId
            if (societeId == null)
            {
                return ResponseEntity.ok(ApiResponse(message = "Veuillez renseigner la societe"))
            }
            var societe = societeRepository.findOneByIdAndDeletedFalse(societeId)
            if (societe == null) {
                return ResponseEntity.ok(ApiResponse(message = "Societe introuvable"))
            }

            if (annexeRepository.existsByEmailAndSocieteAndDeletedFalse(annexeRequest.email, societe))
            {
                return ResponseEntity.ok(ApiResponse(message = "Une annexe avec le meme mail existe deja"))
            }

            var annexe = Annexe()
            annexe.societe = societe
            annexe.zipCode = annexeRequest.zipCode.trim()
            annexe.website = annexeRequest.website.trim()
            annexe.tel = annexeRequest.tel.trim()
            annexe.language = annexeRequest.language.trim()
            annexe.isBillingAddress = annexeRequest.isBillingAddress
            annexe.iban = annexeRequest.iban.trim()
            annexe.email = annexeRequest.email.trim()
            annexe.defaultContact = annexeRequest.defaultContact.trim()
            annexe.currency = annexeRequest.currency.trim()
            annexe.country = annexeRequest.country.trim()
            annexe.city = annexeRequest.city.trim()
            annexe.bankSwift = annexeRequest.bankSwift.trim()
            annexe.accountName = annexeRequest.accountName.trim()
            annexe.VAT = annexeRequest.VAT.trim()
            annexe.address = annexeRequest.address.trim()

            var user: User? = userServices.getCurrentUser()
            annexe.createdBy = user

            annexeRepository.save(annexe)
            return ResponseEntity.ok(ApiResponse(true, "Annexe enregistré avec success", annexe.id))

        }
        catch (e:Exception)
        {
            return ResponseEntity.ok(ApiResponse(message = e.message.toString()))
        }
    }

    @ApiOperation(value = "Modifier une annexe existante")
    @PutMapping("/{id}")
    fun edit(@PathVariable @ApiParam(value = "Id de l'annexe a modifier", name = "id", required = true) id:Long,
             @RequestBody @ApiParam(value = "Instance de la nouvelle annexe", name = "societe", required = true
             ) annexeRequest: NewAnnexeRequest):ResponseEntity<Any>
    {
        try
        {
            var oldAnnexe = annexeRepository.findOneByIdAndDeletedFalse(id)
            if (oldAnnexe == null)
            {
                return ResponseEntity.ok(ApiResponse(message = "Annexe à modifier introuvable"))
            }

            val validationRequest = annexeServices.validerAnnexeRequest(annexeRequest)
            if (validationRequest["valid"] == false)
            {
                return ResponseEntity.ok(ApiResponse(message = validationRequest["message"].toString()))
            }
            var societeId = annexeRequest.societeId
            if (societeId == null)
            {
                return ResponseEntity.ok(ApiResponse(message = "Veuillez renseigner la societe"))
            }
            var societe = societeRepository.findOneByIdAndDeletedFalse(societeId)
            if (societe == null) {
                return ResponseEntity.ok(ApiResponse(message = "Societe introuvable"))
            }

            var annexeExiste = annexeRepository.findOneByEmailAndSocieteAndDeletedFalse(annexeRequest.email, societe)
//            logger.error(annexeExiste!!.id.toString())
//            return ResponseEntity.ok("ok");

            if (annexeExiste !== null)
            {
                logger.error("not null")
                if (annexeExiste.societe !== null)
                {
                    if (annexeExiste.societe!!.id == annexeRequest.societeId)
                    {
                        logger.error(annexeExiste.email)
                        logger.error(annexeRequest.email)

                        if (annexeExiste.email != oldAnnexe.email)
                        {
                            return ResponseEntity.ok(ApiResponse(message = "Une annexe de la societe avec le meme mail existe deja"))
                        }
                    }
                }
            }



            oldAnnexe.societe = societe
            oldAnnexe.zipCode = annexeRequest.zipCode.trim()
            oldAnnexe.website = annexeRequest.website.trim()
            oldAnnexe.tel = annexeRequest.tel.trim()
            oldAnnexe.language = annexeRequest.language.trim()
            oldAnnexe.isBillingAddress = annexeRequest.isBillingAddress
            oldAnnexe.iban = annexeRequest.iban.trim()
            oldAnnexe.email = annexeRequest.email.trim()
            oldAnnexe.defaultContact = annexeRequest.defaultContact.trim()
            oldAnnexe.currency = annexeRequest.currency.trim()
            oldAnnexe.country = annexeRequest.country.trim()
            oldAnnexe.city = annexeRequest.city.trim()
            oldAnnexe.bankSwift = annexeRequest.bankSwift.trim()
            oldAnnexe.accountName = annexeRequest.accountName.trim()
            oldAnnexe.VAT = annexeRequest.VAT.trim()
            oldAnnexe.address = annexeRequest.address.trim()

            var user: User? = userServices.getCurrentUser()

            oldAnnexe.updatedAt = Date()
            oldAnnexe.updatedBy = user

            annexeRepository.save(oldAnnexe)
            return ResponseEntity.ok(oldAnnexe)        }
        catch (e:Exception)
        {
            return ResponseEntity.ok(ApiResponse(message = e.message.toString()))
        }

    }


    @ApiOperation(value = "Liste des centres de facuration", notes = "Permet de rcupérer la liste de tous les centres de facturation")
    @GetMapping("/billing")
    fun listeCentreFacturation(request:HttpServletRequest):ResponseEntity<Any>
    {

        try
        {
            var parameters = apiServices.getParametersFromGetRequest(request)

            var isBillingAddres:Boolean? = true; var accountName:String? = null; var email:String? = null
            var societe:Societe? = null;
            var societeFromRequest = false; var accountNameFromRequest:Boolean = false
            var emailFromRequest:Boolean = false

            if (parameters.containsKey("isBillingAddress"))
            {
                isBillingAddres = parameters["isBillingAddress"].toString() == "1"
            }

            if (parameters.containsKey("societe"))
            {
                societeFromRequest = true
                societe = societeRepository.findOneByIdAndDeletedFalse(parameters["societe"].toString().toLong())
//                if (societe != null) logger.error(societe.id.toString())
            }
            if (parameters.containsKey("accountName"))
            {
                accountName = parameters["accountName"].toString().trim()
                accountNameFromRequest = true
            }
            if (parameters.containsKey("email"))
            {
                email = parameters["email"].toString().trim()
                emailFromRequest = true
            }

            var numeroPage:Int = apiServices.getNumeroPageFromParameters(parameters)
            var nombreElements:Int = apiServices.getNbElementsFromParameters(parameters)
            var pageable:Pageable = PageRequest.of(numeroPage, nombreElements)

            var annexes = annexeRepository.findAll(
                where(AnnexeSpecifications.isBillingAddress(isBillingAddres))
                        ?.and(AnnexeSpecifications.withSociete(societe, societeFromRequest))
                        ?.and(AnnexeSpecifications.likeAccountName(accountName, accountNameFromRequest))
                        ?.and(AnnexeSpecifications.likeEmail(email, emailFromRequest))
                        ?.and(AnnexeSpecifications.isNotDeleted())
            , pageable)

            return ResponseEntity.ok(annexes)


        }
        catch (e:Exception)
        {
           return ResponseEntity.ok(e.message.toString())
        }


    }

}
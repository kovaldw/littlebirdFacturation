package com.wanoon.littlebirdFacturation.controller

import NewAnnexeRequest
import com.wanoon.littlebirdFacturation.model.Annexe
import com.wanoon.littlebirdFacturation.model.Societe
import com.wanoon.littlebirdFacturation.repository.AnnexeRepository
import com.wanoon.littlebirdFacturation.repository.AnnexeSpecifications
import com.wanoon.littlebirdFacturation.repository.SocieteRepository
import com.wanoon.littlebirdFacturation.services.AnnexeServices
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
import javax.servlet.http.HttpServletRequest


@RestController
@RequestMapping("/api/annexe")
@Api(description = "Ce controller gere les annexes des societes")
class AnnexeController
{

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
    fun list():ResponseEntity<Any>
    {
        var annexes:MutableList<Annexe> = ArrayList()
        try
        {
            annexes = annexeRepository.findAll()
            return ResponseEntity.ok(ApiResponse(true, "OK", annexes))
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
            var annexe = annexeRepository.findByIdOrNull(id)

            if (annexe == null)
            {
                return ResponseEntity.ok(ApiResponse(message = "Annexe introuvable"))
            }

            return ResponseEntity.ok(ApiResponse(true, "OK", annexe))

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
            var annexe: Annexe? = annexeRepository.findByIdOrNull(id)

            if (annexe == null)
            {
                return ResponseEntity.ok(ApiResponse(false, "L'annexe a suppprimer nexiste pas"));
            }

            annexeRepository.delete(annexe)

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
            if (!annexeServices.validerAnnexeRequest(annexeRequest))
            {
                return ResponseEntity.ok(ApiResponse(message = "Veuillez vérifier les informations envoyees"))
            }
            var societeId = annexeRequest.societeId
            if (societeId == null)
            {
                return ResponseEntity.ok(ApiResponse(message = "Veuillez renseigner la societe"))
            }
            var societe = societeRepository.findByIdOrNull(societeId)
            if (societe == null) {
                return ResponseEntity.ok(ApiResponse(message = "Societe introuvable"))
            }

            if (annexeRepository.existsByEmailAndSociete(annexeRequest.email, societe))
            {
                return ResponseEntity.ok(ApiResponse(message = "Une annexe avec le meme mail existe deja"))
            }

            var annexe = Annexe()
            annexe.societe = societe
            annexe.zipCode = annexeRequest.zipCode
            annexe.website = annexeRequest.website
            annexe.tel = annexeRequest.tel
            annexe.language = annexeRequest.language
            annexe.isBillingAddress = annexeRequest.isBillingAddress
            annexe.iban = annexeRequest.iban
            annexe.email = annexeRequest.email
            annexe.defaultContact = annexeRequest.defaultContact
            annexe.currency = annexeRequest.currency
            annexe.country = annexeRequest.country
            annexe.city = annexeRequest.city
            annexe.bankSwift = annexeRequest.bankSwift
            annexe.accountName = annexeRequest.accountName
            annexe.VAT = annexeRequest.VAT
            annexe.address = annexeRequest.address

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
            var oldAnnexe = annexeRepository.findByIdOrNull(id)
            if (oldAnnexe == null)
            {
                return ResponseEntity.ok(ApiResponse(message = "Annexe à modifier introuvable"))
            }

            if (!annexeServices.validerAnnexeRequest(annexeRequest))
            {
                return ResponseEntity.ok(ApiResponse(message = "Veuillez vérifier les informations envoyees"))
            }
            var societeId = annexeRequest.societeId
            if (societeId == null)
            {
                return ResponseEntity.ok(ApiResponse(message = "Veuillez renseigner la societe"))
            }
            var societe = societeRepository.findByIdOrNull(societeId)
            if (societe == null) {
                return ResponseEntity.ok(ApiResponse(message = "Societe introuvable"))
            }

            var annexeExiste = annexeRepository.findOneByEmailAndSociete(annexeRequest.email, societe)
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
            oldAnnexe.zipCode = annexeRequest.zipCode
            oldAnnexe.website = annexeRequest.website
            oldAnnexe.tel = annexeRequest.tel
            oldAnnexe.language = annexeRequest.language
            oldAnnexe.isBillingAddress = annexeRequest.isBillingAddress
            oldAnnexe.iban = annexeRequest.iban
            oldAnnexe.email = annexeRequest.email
            oldAnnexe.defaultContact = annexeRequest.defaultContact
            oldAnnexe.currency = annexeRequest.currency
            oldAnnexe.country = annexeRequest.country
            oldAnnexe.city = annexeRequest.city
            oldAnnexe.bankSwift = annexeRequest.bankSwift
            oldAnnexe.accountName = annexeRequest.accountName
            oldAnnexe.VAT = annexeRequest.VAT
            oldAnnexe.address = annexeRequest.address

            annexeRepository.save(oldAnnexe)
            return ResponseEntity.ok(ApiResponse(true, "Annexe modifiee avec success", oldAnnexe.id))        }
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
            var parameters = HashMap<String, Any>()
            var filtresSent = request.parameterMap

            for (filtre in filtresSent)
            {
                parameters[filtre.key] = filtre.value[0]
            }

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
                societe = societeRepository.findByIdOrNull(parameters["societe"].toString().toLong())
                if (societe != null) logger.error(societe.id.toString())
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

            var numeroPage:Int = 0
            var nombreElements:Int = 3
            if (parameters.containsKey("page")) numeroPage = parameters["page"].toString().toInt()
            if (parameters.containsKey("nbElements")) nombreElements = parameters["nbElements"].toString().toInt()

            logger.error("page $numeroPage")
            logger.error("nbElements $nombreElements")

            var pageable:Pageable = PageRequest.of(numeroPage, nombreElements)

            var annexes = annexeRepository.findAll(
                where(AnnexeSpecifications.isBillingAddress(isBillingAddres))
                        ?.and(AnnexeSpecifications.withSociete(societe, societeFromRequest))
                        ?.and(AnnexeSpecifications.likeAccountName(accountName, accountNameFromRequest))
                        ?.and(AnnexeSpecifications.likeEmail(email, emailFromRequest))
            , pageable)
            return ResponseEntity.ok(annexes)


//        for (filtre in filtresSent)
//        {
//            parameters[filtre.key] = filtre.value[0]
//        }

        }
        catch (e:Exception)
        {
           return ResponseEntity.ok(e.message.toString())
        }


    }

}
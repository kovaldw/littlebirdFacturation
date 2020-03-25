package com.wanoon.littlebirdFacturation.controller

import com.wanoon.littlebirdFacturation.model.Product
import com.wanoon.littlebirdFacturation.payload.requests.product.NewProductRequest
import com.wanoon.littlebirdFacturation.repository.ProductRepository
import com.wanoon.littlebirdFacturation.repository.ProductSpecifications
import com.wanoon.littlebirdFacturation.security.services.UserServices
import com.wanoon.littlebirdFacturation.services.ApiServices
import com.wanoon.littlebirdFacturation.services.ProductServices
import com.wanoon.payload.responses.ApiResponse
import io.swagger.annotations.Api
import io.swagger.annotations.ApiOperation
import io.swagger.annotations.ApiParam
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.util.*
import javax.servlet.http.HttpServletRequest
import kotlin.collections.HashMap

@RestController
@RequestMapping("api/product")
@Api(description = "Controller gere les produits")

class ProductController
{
    @Autowired
    lateinit var userServices: UserServices

    @Autowired
    lateinit var apiServices: ApiServices

    @Autowired
    lateinit var productServices: ProductServices

    @Autowired
    lateinit var productRepository: ProductRepository


    @GetMapping("/")
    @ApiOperation(value = "Liste des produits", notes = "Cette méthode permet de récupérer la liste des produits non supprimés")
    fun list(request:HttpServletRequest):ResponseEntity<Any>
    {
        try
        {
            val parameters = apiServices.getParametersFromGetRequest(request.parameterMap)

            var numeroPage:Int = apiServices.getNumeroPageFromParameters(parameters)
            var nombreElements:Int = apiServices.getNbElementsFromParameters(parameters)

            var pageable:Pageable = PageRequest.of(numeroPage, nombreElements)

            val products = productRepository.findAll(ProductSpecifications.isNotDeleted(), pageable)

            return ResponseEntity.ok(products)

        }
        catch (e:Exception)
        {
            return ResponseEntity.ok(ApiResponse(message = "Erreur: ${e.message.toString()}"))
        }
    }


    @GetMapping("/{id}")
    @ApiOperation(value = "Intance d'un produit", notes = "Cette méthode permet de récupérer l'instance d'un produit grace à son ID")
    fun one(@ApiParam(value = "ID du produit à récupérer", name = "id", required = true)
            @PathVariable id:Long):ResponseEntity<Any>
    {
        try
        {
            var product:Product? = productRepository.findOneByIdAndDeletedFalse(id)
                    ?: return ResponseEntity.ok(ApiResponse(message = "Produit introuvable"))

            return ResponseEntity.ok(product!!)
        }
        catch (e:Exception)
        {
            return ResponseEntity.ok(ApiResponse(message = "Erreur: ${e.message.toString()}"))
        }
    }


    @DeleteMapping("/{id}")
    @ApiOperation(value = "Suppression d'un produit", notes = "Permet d supprimer un produit grace à son ID")
    fun delete(
            @ApiParam(value = "ID du produit à supprimer", name = "id")
            @PathVariable id:Long):ResponseEntity<Any>
    {
        try
        {
            var product = productRepository.findOneByIdAndDeletedFalse(id)
                    ?: return ResponseEntity.ok(ApiResponse(message = "Produit a supprimer introuvable"))

            val user = userServices.getCurrentUser()

            product.deleted = true
            product.deletedAt = Date()
            product.deletedBy = user

            productRepository.save(product)

            return ResponseEntity.ok(ApiResponse(success = true, message = "Suppression effectuee avec succes"))
        }
        catch (e:Exception)
        {
            return ResponseEntity.ok(ApiResponse(message = "Erreur: ${e.message.toString()}"))
        }
    }


    @PostMapping("/")
    @ApiOperation(value = "Creation d'un produit", notes = "Permet de créer une nouvelle instance de produit")
    fun create(
            @ApiParam(value = "Instance du produit", name = "product", required = true)
            @RequestBody productRequest: NewProductRequest):ResponseEntity<Any>
    {

        try
        {
            if (!productServices.validerProductRequest(productRequest))
            {
                return ResponseEntity.ok(ApiResponse(message = "Veuillez vérifier les informations invoyees"))
            }

            if (productRepository.existsByProductCodeAndDeletedFalse(productRequest.productCode))
            {
                return ResponseEntity.ok(ApiResponse(message = "Un produit du meme code existe deja"))
            }

            var product = Product(description = productRequest.description, gazoil = productRequest.gazoil,
                price = productRequest.price, productCode = productRequest.productCode, vat = productRequest.vat)

            val user = userServices.getCurrentUser()
            product.createdBy = user

            productRepository.save(product)

            return ResponseEntity.ok(ApiResponse(success = true, message = "produit créé avec succès", response = product.id))

        }
        catch (e:Exception)
        {
            return ResponseEntity.ok(ApiResponse(message = "Erreur: ${e.message.toString()}"))
        }
    }


    @PutMapping("/{id}")
    @ApiOperation(value = "Modification d'un produit", notes = "Modifier un produit grace à son ID")
    fun update(
            @PathVariable id:Long,
            @ApiParam(value = "Instance du nouveau produit", name = "product", required = true)
            @RequestBody productRequest: NewProductRequest
    ):ResponseEntity<Any>
    {

        try
        {
            var product = productRepository.findOneByIdAndDeletedFalse(id)
            if (product == null)
            {
                return ResponseEntity.ok(ApiResponse(message = "Produit à modifier introuvable"))
            }

            if (!productServices.validerProductRequest(productRequest))
            {
                return ResponseEntity.ok(ApiResponse(message = "Veuillez vérifier les informations envoyées"))
            }

            var productExiste = productRepository.findOneByProductCodeAndDeletedFalse(productRequest.productCode)
            if (productExiste != null && product.productCode != productExiste.productCode)
            {
                return ResponseEntity.ok(ApiResponse(message = "Un autre produit du meme code existe deja"))
            }

            val user = userServices.getCurrentUser()

            product.description = productRequest.description
            product.gazoil = productRequest.gazoil
            product.productCode = productRequest.productCode
            product.price = productRequest.price
            product.vat = productRequest.vat

            product.updatedAt = Date()
            product.updatedBy = user

            productRepository.save(product)

            return ResponseEntity.ok(product)
        }
        catch (e:Exception)
        {
            return ResponseEntity.ok(ApiResponse(message = "Erreur: ${e.message.toString()}"))
        }

    }

}
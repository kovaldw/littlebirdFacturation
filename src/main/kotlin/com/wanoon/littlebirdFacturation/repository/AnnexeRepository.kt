package com.wanoon.littlebirdFacturation.repository

import com.wanoon.littlebirdFacturation.model.Annexe
import com.wanoon.littlebirdFacturation.model.Societe
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import java.awt.print.Book
import java.util.*
import javax.persistence.EntityManager
import javax.persistence.PersistenceContext
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root


interface AnnexeRepository : JpaRepository<Annexe, Long>, JpaSpecificationExecutor<Annexe>
{


    companion object
    {
        @PersistenceContext
        lateinit var em:EntityManager

    }

    fun existsByEmailAndSociete(email:String, societe: Societe):Boolean
    fun findOneByEmailAndSociete(email:String, societe: Societe):Annexe?


//    fun findByIsBillingAddress(isBillingAddres:Boolean):ArrayList<Annexe>
    fun findByIsBillingAddressAndSociete(isBillingAddres:Boolean, societe: Societe):ArrayList<Annexe>

//    @Query("SELECT a FROM Annexe " +
//            "WHERE a.isBillingAddres = :isBillingAddress AND " +
//            "(a.societe IN :societes OR a.accountName LIKE :accountName)" +
//            "")
//    fun recupererCentreFacturation(
//            @Param("isBillingAddress") isBillingAddres: Boolean = true,
//            @Param("societes")
//    ):ArrayList<Annexe>

//
//    fun findByIsBillingAddress(isBillingAddres: Boolean? =  null,
//                                      accountName:String? = null,
//                                      address:String? = null,
//                                      bankSwift:String? = null,
//                                      city:String? = null,
//                                      country:String? = null,
//                                      currency:String? = null,
//                                      defaultContact:String? = null,
//                                      email:String? = null,
//                                      iban:String? = null,
//                                      language:String? = null,
//                                      tel:String? = null,
//                                      website:String? = null,
//                                      zipCode:String? = null,
//                                      vat:String? = null,
//                                      societe: Societe? = null
//    ): MutableList<Annexe>? {
//        val cb = em.criteriaBuilder
//        val cq: CriteriaQuery<Annexe> = cb.createQuery(Annexe::class.java)
//        val root: Root<Annexe> = cq.from(Annexe::class.java)
//        val predicates: MutableList<Predicate> = ArrayList<Predicate>()
//        if (isBillingAddres != null)
//        {
//            predicates.add(cb.equal(root.get<Any>("isBillingAddress"), isBillingAddres))
//        }
//        if (vat != null)
//        {
//            predicates.add(cb.like(root.get("VAT"), "%$vat%"))
//        }
//        if (address !== null)
//        {
//            predicates.add(cb.like(root.get("address"), "%$vat%" ))
//        }
//        if (bankSwift !== null)
//        {
//            predicates.add(cb.like(root.get("bankSwift"), "%$bankSwift%"))
//        }
//        if (city !== null)
//        {
//            predicates.add(cb.like(root.get("city"), "%$city%"))
//        }
//        if (country !== null)
//        {
//            predicates.add(cb.like(root.get("country"), "%$country%"))
//        }
//        if (currency !== null)
//        {
//            predicates.add(cb.like(root.get("currency"), "%$currency%"))
//        }
//        if (defaultContact !== null)
//        {
//            predicates.add(cb.like(root.get("defaultContact"), "%$defaultContact%"))
//        }
//        if (email !== null)
//        {
//            predicates.add(cb.like(root.get("email"), "%$email%"))
//        }
//        if (iban !== null)
//        {
//            predicates.add(cb.like(root.get("iban"), "%$iban%"))
//        }
//        if (language !== null)
//        {
//            predicates.add(cb.like(root.get("language"), "%$language%"))
//        }
//        if (tel !== null)
//        {
//            predicates.add(cb.like(root.get("tel"), "%$tel%"))
//        }
//        if (website !== null)
//        {
//            predicates.add(cb.like(root.get("website"), "%$website%"))
//        }
//        if (zipCode !== null)
//        {
//            predicates.add(cb.like(root.get("zipCode"), "%$zipCode%"))
//        }
//        if (societe !== null)
//        {
//            predicates.add(cb.like(root.get("societe"), "%$societe%"))
//        }
//        cq.where(predicates[0])
////        cq.where(predicates.toArray(arrayOfNulls<Predicate>(0)))
//        return em.createQuery(cq).getResultList()
//    }


}
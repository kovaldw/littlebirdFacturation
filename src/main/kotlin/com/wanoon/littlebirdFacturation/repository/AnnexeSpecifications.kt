package com.wanoon.littlebirdFacturation.repository

import com.wanoon.littlebirdFacturation.model.Annexe
import com.wanoon.littlebirdFacturation.model.Societe
import org.springframework.data.jpa.domain.Specification
import java.time.LocalDate
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root


class AnnexeSpecifications
{

    companion object
    {
        fun isBillingAddress(isBillingAddress:Boolean?): Specification<Annexe>? {
            return object : Specification<Annexe>
            {
//                override fun toPredicate(p0: Root<Annexe>, p1: CriteriaQuery<*>, p2: CriteriaBuilder): Predicate? {
//                    p0.get<Annexe>("")
//                }
                override fun toPredicate(root: Root<Annexe>, query: CriteriaQuery<*>, builder: CriteriaBuilder): Predicate
                {
                    return builder.equal( root.get<Annexe>("isBillingAddress"), isBillingAddress )
                }
            }
        }

        fun withSociete(societe:Societe?, fromRequest:Boolean = false): Specification<Annexe>? {
//            if (societe == null) return null

            if (fromRequest)
            {
                return Specification<Annexe> { root, query, builder ->
                    builder.equal( root.get<Annexe>("societe"), societe )
                }
            }
            if (!fromRequest && societe == null)
            {
                return null
            }


            return Specification<Annexe> { root, query, builder ->
                //                    return builder.equal( root.join("societe").get<Annexe>("societeId"), societeId )
                builder.equal( root.get<Annexe>("societe"), societe )
            }
        }


        fun likeAccountName(accountName:String?, fromRequest: Boolean = false): Specification<Annexe>? {

            if (fromRequest)
            {
                return Specification<Annexe> { root, query, builder ->
                    builder.like( root.get("accountName"), "%$accountName%" )
                }
            }
            if (!fromRequest && accountName == null)
            {
                return null
            }

            return Specification<Annexe> { root, query, builder ->
                //                    return builder.equal( root.join("societe").get<Annexe>("societeId"), societeId )
                builder.like( root.get("accountName"), "%$accountName%" )
            }
        }

        fun likeEmail(email:String?, fromRequest: Boolean = false): Specification<Annexe>? {

            if (fromRequest)
            {
                return Specification<Annexe> { root, query, builder ->
                    builder.like( root.get("email"), "%$email%" )
                }
            }
            if (!fromRequest && email == null)
            {
                return null
            }

            return Specification<Annexe> { root, query, builder ->
                //                    return builder.equal( root.join("societe").get<Annexe>("societeId"), societeId )
                builder.like( root.get("accountName"), "%$email%" )
            }
        }
    }






}
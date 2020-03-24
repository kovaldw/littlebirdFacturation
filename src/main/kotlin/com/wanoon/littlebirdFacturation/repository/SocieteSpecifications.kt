package com.wanoon.littlebirdFacturation.repository

import com.wanoon.littlebirdFacturation.model.Annexe
import com.wanoon.littlebirdFacturation.model.Societe
import org.springframework.data.jpa.domain.Specification
import java.time.LocalDate
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Predicate
import javax.persistence.criteria.Root


class SocieteSpecifications
{

    companion object
    {
        fun isNotDeleted(deleted:Boolean? = false): Specification<Societe>? {
            return object : Specification<Societe>
            {
//                override fun toPredicate(p0: Root<Annexe>, p1: CriteriaQuery<*>, p2: CriteriaBuilder): Predicate? {
//                    p0.get<Annexe>("")
//                }
                override fun toPredicate(root: Root<Societe>, query: CriteriaQuery<*>, builder: CriteriaBuilder): Predicate
                {
                    return builder.equal( root.get<Societe>("deleted"), deleted )
                }
            }
        }


    }






}
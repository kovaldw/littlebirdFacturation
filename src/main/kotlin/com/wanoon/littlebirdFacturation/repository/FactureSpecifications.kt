package com.wanoon.littlebirdFacturation.repository

import com.sun.org.apache.xpath.internal.operations.Bool
import com.wanoon.littlebirdFacturation.model.Facture
import org.springframework.data.jpa.domain.Specification
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Root

class FactureSpecifications
{
    companion object
    {
        fun isNotDeleted(deleted:Boolean = false):Specification<Facture>
        {
            return Specification<Facture> { root: Root<Facture>, criteriaQuery: CriteriaQuery<*>, criteriaBuilder: CriteriaBuilder ->
                criteriaBuilder.equal(root.get<Facture>("deleted"), deleted)
            }
        }
    }

}
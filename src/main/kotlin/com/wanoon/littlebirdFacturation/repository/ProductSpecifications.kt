package com.wanoon.littlebirdFacturation.repository

import com.wanoon.littlebirdFacturation.model.Product
import org.springframework.data.jpa.domain.Specification
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Root

class ProductSpecifications
{
    companion object
    {
        fun isNotDeleted(deleted:Boolean=false):Specification<Product>?
        {
            return Specification<Product>{ root: Root<Product>, criteriaQuery: CriteriaQuery<*>, criteriaBuilder: CriteriaBuilder ->
                criteriaBuilder.equal(root.get<Product>("deleted"), deleted)
            }
        }
    }

}
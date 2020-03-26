package com.wanoon.littlebirdFacturation.repository

import com.wanoon.littlebirdFacturation.model.Facture
import com.wanoon.littlebirdFacturation.model.LigneFacturation
import org.springframework.data.jpa.domain.Specification
import javax.persistence.criteria.CriteriaBuilder
import javax.persistence.criteria.CriteriaQuery
import javax.persistence.criteria.Root

class LigneFacturationSpecifications
{

    companion object
    {
        fun isNotDeleted(deleted:Boolean = false):Specification<LigneFacturation>
        {
            return Specification<LigneFacturation>{ root: Root<LigneFacturation>, criteriaQuery: CriteriaQuery<*>, criteriaBuilder: CriteriaBuilder ->
                criteriaBuilder.equal(root.get<LigneFacturation>("deleted"), false)
            }
        }

        fun withFacture(facture:Facture?, fromRequest:Boolean = false):Specification<LigneFacturation>?
        {
            if (fromRequest)
            {
                return Specification<LigneFacturation>{ root: Root<LigneFacturation>, criteriaQuery: CriteriaQuery<*>, criteriaBuilder: CriteriaBuilder ->
                    criteriaBuilder.equal(root.get<LigneFacturation>("facture"), facture)

                }
            }

            if (!fromRequest && facture == null) return null

            return Specification<LigneFacturation>{ root: Root<LigneFacturation>, criteriaQuery: CriteriaQuery<*>, criteriaBuilder: CriteriaBuilder ->
                criteriaBuilder.equal(root.get<LigneFacturation>("facture"), facture)

            }
        }


        fun withColis(colis:Long?, fromRequest:Boolean = false):Specification<LigneFacturation>?
        {
            if (fromRequest)
            {
                return Specification<LigneFacturation>{ root: Root<LigneFacturation>, criteriaQuery: CriteriaQuery<*>, criteriaBuilder: CriteriaBuilder ->
                    criteriaBuilder.equal(root.get<LigneFacturation>("colisId"), colis)

                }
            }

            if (!fromRequest && colis == null) return null

            return Specification<LigneFacturation>{ root: Root<LigneFacturation>, criteriaQuery: CriteriaQuery<*>, criteriaBuilder: CriteriaBuilder ->
                criteriaBuilder.equal(root.get<LigneFacturation>("colisId"), colis)

            }
        }
    }
}
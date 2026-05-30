package com.eventoutfit.repository;

import com.eventoutfit.model.Outfit;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OutfitRepositoryImpl implements OutfitRepositoryCustom {

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Outfit> findOutfitsWithMinPrice(double minPrice) {
        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<Outfit> cq = cb.createQuery(Outfit.class);
        Root<Outfit> root = cq.from(Outfit.class);

        cq.where(cb.greaterThanOrEqualTo(root.get("price"), minPrice));
        cq.orderBy(cb.desc(root.get("createdAt")));

        return entityManager.createQuery(cq).getResultList();
    }
}
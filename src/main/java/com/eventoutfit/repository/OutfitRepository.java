package com.eventoutfit.repository;

import com.eventoutfit.model.Outfit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface OutfitRepository extends JpaRepository<Outfit, Long>, OutfitRepositoryCustom {

    List<Outfit> findByUserId(Long userId);

    @Query("SELECT o FROM Outfit o WHERE o.event.id = :eventId AND o.gender = :gender ORDER BY o.createdAt DESC")
    List<Outfit> findByEventAndGenderSortedByDate(@Param("eventId") Long eventId, @Param("gender") String gender);

    @Query("SELECT o FROM Outfit o WHERE LOWER(o.name) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Outfit> searchByNameKeyword(@Param("keyword") String keyword);

    @Query("SELECT o FROM Outfit o WHERE o.id IN " +
            "(SELECT c.outfit.id FROM Comment c GROUP BY c.outfit.id HAVING COUNT(c) > 2)")

    List<Outfit> findOutfitsWithMoreThanTwoComments();
}
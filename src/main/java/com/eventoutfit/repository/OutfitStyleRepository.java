package com.eventoutfit.repository;

import com.eventoutfit.model.OutfitStyle;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface OutfitStyleRepository extends JpaRepository<OutfitStyle, Long> {
}
package com.eventoutfit.repository;

import com.eventoutfit.model.Outfit;
import java.util.List;

public interface OutfitRepositoryCustom {
    List<Outfit> findOutfitsWithMinPrice(double minPrice);
}
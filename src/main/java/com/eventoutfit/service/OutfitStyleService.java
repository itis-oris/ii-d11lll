package com.eventoutfit.service;

import com.eventoutfit.model.OutfitStyle;
import com.eventoutfit.repository.OutfitStyleRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OutfitStyleService {

    private static final Logger logger = LoggerFactory.getLogger(OutfitStyleService.class);

    private final OutfitStyleRepository styleRepository;

    public OutfitStyleService(OutfitStyleRepository styleRepository) {
        this.styleRepository = styleRepository;
    }

    public List<OutfitStyle> getAllStyles() {
        logger.debug("Получение всех стилей");
        return styleRepository.findAll();
    }

    public Optional<OutfitStyle> findById(Long id) {
        logger.debug("Поиск стиля по ID: {}", id);
        return styleRepository.findById(id);
    }

    public List<OutfitStyle> findByIds(List<Long> ids) {
        logger.debug("Поиск стилей по списку ID: {}", ids);
        if (ids == null || ids.isEmpty()) {
            return List.of();
        }
        return styleRepository.findAllById(ids);
    }
}
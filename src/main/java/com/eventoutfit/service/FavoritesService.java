package com.eventoutfit.service;

import com.eventoutfit.model.Outfit;
import com.eventoutfit.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class FavoritesService {

    private static final Logger logger = LoggerFactory.getLogger(FavoritesService.class);

    private final UserRepository userRepository;
    private final OutfitService outfitService;

    public FavoritesService(UserRepository userRepository, OutfitService outfitService) {
        this.userRepository = userRepository;
        this.outfitService = outfitService;
    }

    public List<Outfit> getUserFavorites(Long userId) {
        logger.debug("Получение избранного пользователя: userId={}", userId);

        return userRepository.findById(userId)
                .map(user -> new ArrayList<>(user.getFavoriteOutfits()))
                .orElse(new ArrayList<>());
    }

    public boolean isFavorite(Long userId, Long outfitId) {
        logger.debug("Проверка избранного: userId={}, outfitId={}", userId, outfitId);

        return userRepository.findById(userId)
                .map(user -> user.getFavoriteOutfits().stream()
                        .anyMatch(outfit -> outfit.getId().equals(outfitId)))
                .orElse(false);
    }


    @Transactional
    public boolean addToFavorites(Long userId, Long outfitId) {
        logger.info("Добавление в избранное: userId={}, outfitId={}", userId, outfitId);

        if (!outfitService.findById(outfitId).isPresent()) {
            logger.warn("Добавление в избранное отклонено: образ не найден");
            return false;
        }

        return userRepository.findById(userId).map(user -> {
            Set<Outfit> favorites = user.getFavoriteOutfits();

            if (favorites.stream().anyMatch(o -> o.getId().equals(outfitId))) {
                logger.debug("Образ уже в избранном: outfitId={}", outfitId);
                return true;
            }

            outfitService.findById(outfitId).ifPresent(favorites::add);
            userRepository.save(user);

            logger.info("Образ добавлен в избранное: userId={}, outfitId={}", userId, outfitId);
            return true;
        }).orElse(false);
    }


    @Transactional
    public boolean removeFromFavorites(Long userId, Long outfitId) {
        logger.info("Удаление из избранного: userId={}, outfitId={}", userId, outfitId);

        return userRepository.findById(userId).map(user -> {
            Set<Outfit> favorites = user.getFavoriteOutfits();

            boolean removed = favorites.removeIf(outfit -> outfit.getId().equals(outfitId));

            if (removed) {
                userRepository.save(user);
                logger.info("Образ удален из избранного: userId={}, outfitId={}", userId, outfitId);
            } else {
                logger.debug("Образ не был в избранном: outfitId={}", outfitId);
            }

            return removed;
        }).orElse(false);
    }
}
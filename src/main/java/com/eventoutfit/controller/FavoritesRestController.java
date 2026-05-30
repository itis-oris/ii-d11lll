package com.eventoutfit.controller;

import com.eventoutfit.model.Outfit;
import com.eventoutfit.service.FavoritesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/favorites")
public class FavoritesRestController {

    private static final Logger logger = LoggerFactory.getLogger(FavoritesRestController.class);
    private final FavoritesService favoritesService;

    public FavoritesRestController(FavoritesService favoritesService) {
        this.favoritesService = favoritesService;
    }

    @GetMapping("/{userId}")
    public ResponseEntity<List<Outfit>> getUserFavorites(@PathVariable Long userId) {
        logger.debug("REST: получение избранного пользователя: {}", userId);
        List<Outfit> favorites = favoritesService.getUserFavorites(userId);
        return ResponseEntity.ok(favorites);
    }

    @GetMapping("/{userId}/check/{outfitId}")
    public ResponseEntity<Map<String, Boolean>> checkFavorite(
            @PathVariable Long userId,
            @PathVariable Long outfitId) {
        logger.debug("REST: проверка избранного: userId={}, outfitId={}", userId, outfitId);
        boolean isFavorite = favoritesService.isFavorite(userId, outfitId);

        Map<String, Boolean> response = new HashMap<>();
        response.put("favorite", isFavorite);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/{userId}/{outfitId}")
    public ResponseEntity<Map<String, String>> addToFavorites(
            @PathVariable Long userId,
            @PathVariable Long outfitId) {
        logger.debug("REST: добавление в избранное: userId={}, outfitId={}", userId, outfitId);

        boolean success = favoritesService.addToFavorites(userId, outfitId);

        Map<String, String> response = new HashMap<>();

        if (success) {
            response.put("status", "SUCCESS");
            response.put("message", "Добавлено в избранное");
            return ResponseEntity.ok(response);
        } else {
            response.put("status", "ERROR");
            response.put("message", "Не удалось добавить в избранное");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping("/{userId}/{outfitId}")
    public ResponseEntity<Map<String, String>> removeFromFavorites(
            @PathVariable Long userId,
            @PathVariable Long outfitId) {
        logger.debug("REST: удаление из избранного: userId={}, outfitId={}", userId, outfitId);

        boolean success = favoritesService.removeFromFavorites(userId, outfitId);

        Map<String, String> response = new HashMap<>();

        if (success) {
            response.put("status", "SUCCESS");
            response.put("message", "Удалено из избранного");
            return ResponseEntity.ok(response);
        } else {
            response.put("status", "ERROR");
            response.put("message", "Не удалось удалить из избранного");
            return ResponseEntity.badRequest().body(response);
        }
    }
}
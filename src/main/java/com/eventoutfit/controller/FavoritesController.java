package com.eventoutfit.controller;

import com.eventoutfit.model.Outfit;
import com.eventoutfit.model.User;
import com.eventoutfit.service.FavoritesService;
import com.eventoutfit.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class FavoritesController {

    private static final Logger logger = LoggerFactory.getLogger(FavoritesController.class);
    private final FavoritesService favoritesService;
    private final UserService userService;

    public FavoritesController(FavoritesService favoritesService, UserService userService) {
        this.favoritesService = favoritesService;
        this.userService = userService;
    }

    @GetMapping("/favorites")
    public String favourites(Model model) {
        User user = userService.getCurrentUser();
        if (user == null) {
            return "redirect:/login";
        }

        List<Outfit> favorites = favoritesService.getUserFavorites(user.getId());
        model.addAttribute("favorites", favorites);
        return "favorites";
    }

    @PostMapping("/favorites/add")
    @ResponseBody
    public String addToFavorites(@RequestParam Long outfitId) {
        User user = userService.getCurrentUser();
        if (user == null) {
            return "NOT_LOGGED_IN";
        }

        boolean success = favoritesService.addToFavorites(user.getId(), outfitId);
        return success ? "SUCCESS" : "ALREADY_FAVORITE";
    }

    @PostMapping("/favorites/remove")
    @ResponseBody
    public String removeFromFavorites(@RequestParam Long outfitId) {
        User user = userService.getCurrentUser();
        if (user == null) {
            return "NOT_LOGGED_IN";
        }

        boolean success = favoritesService.removeFromFavorites(user.getId(), outfitId);
        return success ? "SUCCESS" : "NOT_IN_FAVORITES";
    }
}
package com.eventoutfit.controller;

import com.eventoutfit.model.Outfit;
import com.eventoutfit.service.OutfitService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
public class OutfitCatalogController {

    private static final Logger logger = LoggerFactory.getLogger(OutfitCatalogController.class);
    private final OutfitService outfitService;

    public OutfitCatalogController(OutfitService outfitService) {
        this.outfitService = outfitService;
    }

    @GetMapping("/outfit-catalog")
    public String catalog(@RequestParam Long eventId,
                          @RequestParam String gender,
                          Model model) {

        List<Outfit> outfits = outfitService.getOutfitsByEventAndGender(eventId, gender);

        model.addAttribute("outfits", outfits);
        model.addAttribute("eventId", eventId);
        model.addAttribute("gender", gender);
        model.addAttribute("genderDisplay", "FEMALE".equals(gender) ? "Женские образы" : "Мужские образы");

        return "outfit-catalog";
    }

    @GetMapping("/search")
    public String search(@RequestParam(required = false) String keyword, Model model) {
        List<Outfit> results = outfitService.searchByKeyword(keyword);

        model.addAttribute("results", results);
        model.addAttribute("keyword", keyword);
        model.addAttribute("resultCount", results.size());

        return "search-results";
    }

}
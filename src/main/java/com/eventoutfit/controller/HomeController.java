package com.eventoutfit.controller;

import com.eventoutfit.model.Event;
import com.eventoutfit.model.Outfit;
import com.eventoutfit.model.User;
import com.eventoutfit.repository.EventRepository;
import com.eventoutfit.service.EventService;
import com.eventoutfit.service.UserService;
import com.eventoutfit.service.OutfitService;
import com.eventoutfit.repository.OutfitRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HomeController {

    private static final Logger logger = LoggerFactory.getLogger(HomeController.class);
    private final EventService eventService;
    private final UserService userService;
    private final OutfitService outfitService;
    private final OutfitRepository outfitRepository;

    public HomeController(EventService eventService, UserService userService, OutfitService outfitService, OutfitRepository outfitRepository) {
        this.eventService = eventService;
        this.userService = userService;
        this.outfitService = outfitService;
        this.outfitRepository = outfitRepository;
    }

    @GetMapping("/home")
    public String home(Model model) {
        User user = userService.getCurrentUser();
        if (user != null) {
            model.addAttribute("username", user.getUsername());
        }

        List<Event> events = eventService.getAllEvents();
        model.addAttribute("events", events);

        List<Outfit> popular = outfitService.getOutfitsWithManyComments();
        model.addAttribute("popularOutfits", popular);

        List<Outfit> expensiveOutfits = outfitRepository.findOutfitsWithMinPrice(50000);
        model.addAttribute("expensiveOutfits", expensiveOutfits);

        return "home";
    }
}
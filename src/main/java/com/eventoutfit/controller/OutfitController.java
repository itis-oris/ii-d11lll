package com.eventoutfit.controller;

import com.eventoutfit.model.Outfit;
import com.eventoutfit.model.User;
import com.eventoutfit.service.EventService;
import com.eventoutfit.service.OutfitService;
import com.eventoutfit.service.OutfitStyleService;
import com.eventoutfit.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.List;

@Controller
public class OutfitController {

    private static final Logger logger = LoggerFactory.getLogger(OutfitController.class);
    private final OutfitService outfitService;
    private final EventService eventService;
    private final OutfitStyleService styleService;
    private final UserService userService;

    public OutfitController(OutfitService outfitService,
                            EventService eventService,
                            OutfitStyleService styleService,
                            UserService userService) {
        this.outfitService = outfitService;
        this.eventService = eventService;
        this.styleService = styleService;
        this.userService = userService;
    }

    @GetMapping("/outfits")
    public String myOutfits(Model model) {
        User user = userService.getCurrentUser();
        if (user == null) {
            return "redirect:/login";
        }

        List<Outfit> outfits = outfitService.getUserOutfits(user.getId());
        model.addAttribute("outfits", outfits);
        return "my-outfits";
    }

    @GetMapping("/outfits/create")
    public String createForm(Model model) {
        User user = userService.getCurrentUser();
        if (user == null) {
            return "redirect:/login";
        }

        model.addAttribute("events", eventService.getAllEvents());
        model.addAttribute("styles", styleService.getAllStyles());
        return "create-outfit";
    }

    @PostMapping("/outfits/create")
    public String createOutfit(@RequestParam String name,
                               @RequestParam(required = false) String description,
                               @RequestParam String gender,
                               @RequestParam Long eventId,
                               @RequestParam(required = false) String[] imageUrls,
                               @RequestParam(required = false) Long[] styleIds,
                               @RequestParam(required = false) Double price,
                               RedirectAttributes redirect) {

        User user = userService.getCurrentUser();
        if (user == null) {
            return "redirect:/login";
        }

        List<String> imageUrlList = imageUrls != null ? Arrays.asList(imageUrls) : null;
        List<Long> styleIdList = styleIds != null ? Arrays.asList(styleIds) : null;

        boolean success = outfitService.createOutfit(name, description, gender, user.getId(),
                imageUrlList, eventId, styleIdList, price);

        if (success) {
            redirect.addFlashAttribute("success", "Образ создан!");
        } else {
            redirect.addFlashAttribute("error", "Ошибка при создании образа");
        }

        return "redirect:/outfits";
    }

    @GetMapping("/outfits/edit")
    public String editForm(@RequestParam Long id, Model model) {
        User user = userService.getCurrentUser();
        if (user == null) {
            return "redirect:/login";
        }

        Outfit outfit = outfitService.findById(id).orElse(null);
        if (outfit == null || !outfit.getUser().getId().equals(user.getId())) {
            return "redirect:/outfits";
        }

        model.addAttribute("outfit", outfit);
        model.addAttribute("events", eventService.getAllEvents());
        model.addAttribute("styles", styleService.getAllStyles());
        return "edit-outfit";
    }

    @PostMapping("/outfits/edit")
    public String updateOutfit(@RequestParam Long id,
                               @RequestParam String name,
                               @RequestParam(required = false) String description,
                               @RequestParam String gender,
                               @RequestParam Long eventId,
                               @RequestParam(required = false) String[] imageUrls,
                               @RequestParam(required = false) Long[] styleIds,
                               RedirectAttributes redirect) {

        User user = userService.getCurrentUser();
        if (user == null) {
            return "redirect:/login";
        }

        List<String> imageUrlList = imageUrls != null ? Arrays.asList(imageUrls) : null;
        List<Long> styleIdList = styleIds != null ? Arrays.asList(styleIds) : null;

        boolean success = outfitService.updateOutfit(id, name, description, gender, eventId, styleIdList);

        if (imageUrlList != null && !imageUrlList.isEmpty()) {
            outfitService.addImagesToOutfit(id, imageUrlList);
        }

        if (success) {
            redirect.addFlashAttribute("success", "Образ обновлён!");
        } else {
            redirect.addFlashAttribute("error", "Ошибка при обновлении образа");
        }

        return "redirect:/outfits";
    }

    @GetMapping("/outfits/delete")
    public String deleteOutfit(@RequestParam Long id, RedirectAttributes redirect) {
        User user = userService.getCurrentUser();
        if (user == null) {
            return "redirect:/login";
        }

        boolean success = outfitService.deleteOutfit(id);

        if (success) {
            redirect.addFlashAttribute("success", "Образ удалён!");
        } else {
            redirect.addFlashAttribute("error", "Ошибка при удалении образа");
        }

        return "redirect:/outfits";
    }

    @PostMapping("/outfits/deleteImage")
    public String deleteImage(@RequestParam Long imageId) {
        User user = userService.getCurrentUser();
        if (user == null) {
            return "error";
        }

        outfitService.deleteImage(imageId);
        return "success";
    }
}
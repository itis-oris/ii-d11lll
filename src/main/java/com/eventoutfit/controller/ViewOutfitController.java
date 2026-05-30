package com.eventoutfit.controller;

import com.eventoutfit.model.Outfit;
import com.eventoutfit.model.User;
import com.eventoutfit.service.CommentService;
import com.eventoutfit.service.OutfitService;
import com.eventoutfit.service.PriceService;
import com.eventoutfit.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Map;

@Controller
public class ViewOutfitController {

    private static final Logger logger = LoggerFactory.getLogger(ViewOutfitController.class);
    private final OutfitService outfitService;
    private final CommentService commentService;
    private final PriceService priceService;
    private final UserService userService;

    public ViewOutfitController(OutfitService outfitService,
                                CommentService commentService,
                                PriceService priceService,
                                UserService userService) {
        this.outfitService = outfitService;
        this.commentService = commentService;
        this.priceService = priceService;
        this.userService = userService;
    }

    @GetMapping("/view-outfit")
    public String viewOutfit(@RequestParam Long id, Model model) {
        Outfit outfit = outfitService.findById(id).orElse(null);

        if (outfit == null) {
            return "redirect:/home";
        }

        model.addAttribute("outfit", outfit);
        Map<String, String> priceInfo = priceService.getPriceInCurrencies(outfit);
        model.addAttribute("priceInfo", priceInfo);
        model.addAttribute("comments", commentService.getCommentsByOutfit(id));
        return "view-outfit";
    }

    @PostMapping("/view-outfit/add-comment")
    public String addComment(@RequestParam Long outfitId,
                             @RequestParam String text,
                             RedirectAttributes redirect) {

        User user = userService.getCurrentUser();
        if (user == null) {
            return "redirect:/login";
        }

        if (text != null && !text.trim().isEmpty()) {
            commentService.addComment(outfitId, user.getId(), text.trim());
            redirect.addFlashAttribute("success", "Комментарий добавлен");
        } else {
            redirect.addFlashAttribute("error", "Комментарий не может быть пустым");
        }

        return "redirect:/view-outfit?id=" + outfitId;
    }
}
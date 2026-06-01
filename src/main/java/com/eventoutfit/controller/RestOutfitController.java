package com.eventoutfit.controller;

import com.eventoutfit.converter.OutfitConverter;
import com.eventoutfit.dto.OutfitRequestDto;
import com.eventoutfit.dto.OutfitResponseDto;
import com.eventoutfit.model.Outfit;
import com.eventoutfit.model.OutfitImage;
import com.eventoutfit.service.OutfitService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/outfits")
public class RestOutfitController {

    private static final Logger logger = LoggerFactory.getLogger(RestOutfitController.class);
    private final OutfitService outfitService;
    private final OutfitConverter outfitConverter;

    public RestOutfitController(OutfitService outfitService, OutfitConverter outfitConverter) {
        this.outfitService = outfitService;
        this.outfitConverter = outfitConverter;
    }

    @GetMapping
    public ResponseEntity<List<OutfitResponseDto>> getAllOutfits() {
        logger.debug("REST: получение всех образов");
        List<Outfit> outfits = outfitService.findAll();
        List<OutfitResponseDto> dtos = outfitConverter.toResponseDtoList(outfits);
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<OutfitResponseDto> getOutfitById(@PathVariable Long id) {
        logger.debug("REST: получение образа по ID: {}", id);
        Optional<Outfit> outfit = outfitService.findById(id);

        if (outfit.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(outfitConverter.toResponseDto(outfit.get()));
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<OutfitResponseDto>> getOutfitsByUser(@PathVariable Long userId) {
        logger.debug("REST: получение образов пользователя: {}", userId);
        List<Outfit> outfits = outfitService.getUserOutfits(userId);
        List<OutfitResponseDto> dtos = outfitConverter.toResponseDtoList(outfits);
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/event/{eventId}")
    public ResponseEntity<List<OutfitResponseDto>> getOutfitsByEventAndGender(
            @PathVariable Long eventId,
            @RequestParam String gender) {
        logger.debug("REST: фильтрация образов: eventId={}, gender={}", eventId, gender);
        List<Outfit> outfits = outfitService.getOutfitsByEventAndGender(eventId, gender);
        List<OutfitResponseDto> dtos = outfitConverter.toResponseDtoList(outfits);
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/search")
    public ResponseEntity<List<OutfitResponseDto>> searchOutfits(@RequestParam String keyword) {
        logger.debug("REST: поиск образов по ключевому слову: {}", keyword);
        List<Outfit> results = outfitService.searchByKeyword(keyword);
        List<OutfitResponseDto> dtos = outfitConverter.toResponseDtoList(results);
        return ResponseEntity.ok(dtos);
    }

    @GetMapping("/{id}/images")
    public ResponseEntity<List<OutfitImage>> getOutfitImages(@PathVariable Long id) {
        logger.debug("REST: получение фото образа: {}", id);
        Optional<Outfit> outfit = outfitService.findById(id);

        if (outfit.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok(outfit.get().getImages());
    }

    @GetMapping("/popular")
    public ResponseEntity<List<OutfitResponseDto>> getPopularOutfits() {
        logger.debug("REST: популярные образы (подзапрос)");
        List<Outfit> outfits = outfitService.getOutfitsWithManyComments();
        return ResponseEntity.ok(outfitConverter.toResponseDtoList(outfits));
    }

    @PostMapping
    public ResponseEntity<Map<String, Object>> createOutfit(@Valid @RequestBody OutfitRequestDto request,
                                                            BindingResult result) {
        logger.debug("REST: создание образа: {}", request.getName());

        if (result.hasErrors()) {
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("status", "ERROR");
            errorResponse.put("message", result.getAllErrors().get(0).getDefaultMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        }

        boolean success = outfitService.createOutfit(
                request.getName(),
                request.getDescription(),
                request.getGender(),
                request.getUserId(),
                request.getImageUrls(),
                request.getEventId(),
                request.getStyleIds(),
                request.getPrice()
        );

        Map<String, Object> response = new HashMap<>();

        if (success) {
            response.put("status", "SUCCESS");
            response.put("message", "Образ создан");
            return ResponseEntity.ok(response);
        } else {
            response.put("status", "ERROR");
            response.put("message", "Не удалось создать образ");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Map<String, Object>> updateOutfit(
            @PathVariable Long id,
            @Valid @RequestBody UpdateOutfitRequest request,
            BindingResult result) {
        logger.debug("REST: обновление образа: {}", id);

        if (result.hasErrors()) {
            return ResponseEntity.badRequest().body(Map.of("error", result.getAllErrors().get(0).getDefaultMessage()));
        }

        boolean success = outfitService.updateOutfit(
                id,
                request.getName(),
                request.getDescription(),
                request.getGender(),
                request.getEventId(),
                request.getStyleIds(),
                request.getPrice()
        );

        Map<String, Object> response = new HashMap<>();

        if (success) {
            response.put("status", "SUCCESS");
            response.put("message", "Образ обновлён");
            return ResponseEntity.ok(response);
        } else {
            response.put("status", "ERROR");
            response.put("message", "Не удалось обновить образ");
            return ResponseEntity.badRequest().body(response);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String, Object>> deleteOutfit(@PathVariable Long id) {
        logger.debug("REST: удаление образа: {}", id);

        boolean success = outfitService.deleteOutfit(id);

        Map<String, Object> response = new HashMap<>();

        if (success) {
            response.put("status", "SUCCESS");
            response.put("message", "Образ удалён");
            return ResponseEntity.ok(response);
        } else {
            response.put("status", "ERROR");
            response.put("message", "Не удалось удалить образ");
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
        }
    }

    public static class UpdateOutfitRequest {
        @NotBlank(message = "Название не может быть пустым")
        private String name;

        private String description;

        @NotBlank(message = "Укажите пол (MALE/FEMALE)")
        private String gender;

        @NotNull(message = "ID мероприятия обязателен")
        private Long eventId;

        private List<Long> styleIds;

        private Double price;

        public Double getPrice() {return price;}
        public void setPrice(Double price) {this.price = price;}
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        public String getGender() { return gender; }
        public void setGender(String gender) { this.gender = gender; }
        public Long getEventId() { return eventId; }
        public void setEventId(Long eventId) { this.eventId = eventId; }
        public List<Long> getStyleIds() { return styleIds; }
        public void setStyleIds(List<Long> styleIds) { this.styleIds = styleIds; }
    }
}
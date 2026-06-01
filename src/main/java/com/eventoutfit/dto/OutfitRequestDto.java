package com.eventoutfit.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public class OutfitRequestDto {

    @NotBlank(message = "Название не может быть пустым")
    private String name;

    private String description;

    @NotBlank(message = "Укажите пол (MALE/FEMALE)")
    private String gender;

    @NotNull(message = "ID пользователя обязателен")
    private Long userId;

    @NotNull(message = "ID мероприятия обязателен")
    private Long eventId;

    private List<String> imageUrls;

    private List<Long> styleIds;

    private Double price;

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public List<Long> getStyleIds() {
        return styleIds;
    }

    public void setStyleIds(List<Long> styleIds) {
        this.styleIds = styleIds;
    }
}
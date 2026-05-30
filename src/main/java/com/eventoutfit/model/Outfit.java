package com.eventoutfit.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "outfits")
public class Outfit {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    private String gender;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;

    @OneToMany(mappedBy = "outfit", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OutfitImage> images = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "outfit_outfit_styles",
            joinColumns = @JoinColumn(name = "outfit_id"),
            inverseJoinColumns = @JoinColumn(name = "style_id")
    )
    private List<OutfitStyle> styles = new ArrayList<>();

    @OneToMany(mappedBy = "outfit", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @Column(nullable = true)
    private Double price = 0.0;

    public Outfit() {}

    public Double getPrice() { return price; }

    public void setPrice(Double price) { this.price = price; }

    public List<Comment> getComments() {
        return comments;
    }

    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public List<OutfitStyle> getStyles() {
        return styles;
    }

    public void setStyles(List<OutfitStyle> styles) {
        this.styles = styles;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<OutfitImage> getImages() {
        return images;
    }

    public void setImages(List<OutfitImage> images) {
        this.images = images;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Outfit outfit = (Outfit) o;

        if (id == null || outfit.id == null) {
            return name != null && name.equals(outfit.name);
        }

        return id.equals(outfit.id);
    }

    @Override
    public int hashCode() {
        if (id != null) {
            return id.hashCode();
        }
        return name != null ? name.hashCode() : super.hashCode();
    }

    public void addStyle(OutfitStyle style) {
        styles.add(style);
        style.getOutfits().add(this);
    }

    public void removeStyle(OutfitStyle style) {
        styles.remove(style);
        style.getOutfits().remove(this);
    }

}

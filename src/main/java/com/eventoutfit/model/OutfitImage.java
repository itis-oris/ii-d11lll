package com.eventoutfit.model;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "outfit_images")
public class OutfitImage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "image_url", nullable = false)
    private String imageUrl;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();

    @ManyToOne
    @JoinColumn(name = "outfit_id")
    private Outfit outfit;

    public OutfitImage(Outfit outfit, String imageUrl) {
        this.outfit = outfit;
        this.imageUrl = imageUrl;
    }

    public OutfitImage() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Outfit getOutfit() {
        return outfit;
    }

    public void setOutfit(Outfit outfit) {
        this.outfit = outfit;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OutfitImage that = (OutfitImage) o;

        if (id == null || that.id == null) {
            return imageUrl != null && imageUrl.equals(that.imageUrl);
        }

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        if (id != null) {
            return id.hashCode();
        }
        return imageUrl != null ? imageUrl.hashCode() : super.hashCode();
    }

}

package com.eventoutfit.model;

import jakarta.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "outfit_styles")
public class OutfitStyle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    private String description;

    @ManyToMany(mappedBy = "styles")
    private List<Outfit> outfits = new ArrayList<>();

    public OutfitStyle() {}

    public OutfitStyle(String name, String description) {
        this.name = name;
        this.description = description;
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

    public List<Outfit> getOutfits() {
        return outfits;
    }

    public void setOutfits(List<Outfit> outfits) {
        this.outfits = outfits;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OutfitStyle that = (OutfitStyle) o;

        if (id == null || that.id == null) {
            return name != null && name.equals(that.name);
        }

        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        if (id != null) {
            return id.hashCode();
        }
        return name != null ? name.hashCode() : super.hashCode();
    }
}
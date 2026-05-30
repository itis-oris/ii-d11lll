package com.eventoutfit.model;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "events")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(columnDefinition = "TEXT")
    private String description;

    @OneToMany(mappedBy = "event")
    private List<Outfit> outfits = new ArrayList<>();

    public Event() {}

    public Event(String description, String name) {
        this.description = description;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

        Event event = (Event) o;

        if (id == null || event.id == null) {
            return name != null && name.equals(event.name);
        }

        return id.equals(event.id);
    }

    @Override
    public int hashCode() {
        if (id != null) {
            return id.hashCode();
        }
        return name != null ? name.hashCode() : super.hashCode();
    }
}

package com.eventoutfit.converter;

import com.eventoutfit.model.Outfit;
import com.eventoutfit.model.OutfitImage;
import com.eventoutfit.model.OutfitStyle;
import com.eventoutfit.dto.OutfitRequestDto;
import com.eventoutfit.dto.OutfitResponseDto;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OutfitConverter {

    public OutfitResponseDto toResponseDto(Outfit outfit) {
        if (outfit == null) {
            return null;
        }

        OutfitResponseDto dto = new OutfitResponseDto();
        dto.setId(outfit.getId());
        dto.setName(outfit.getName());
        dto.setDescription(outfit.getDescription());
        dto.setGender(outfit.getGender());
        dto.setCreatedAt(outfit.getCreatedAt());
        dto.setPrice(outfit.getPrice());

        if (outfit.getImages() != null) {
            List<String> imageUrls = outfit.getImages().stream()
                    .map(OutfitImage::getImageUrl)
                    .collect(Collectors.toList());
            dto.setImageUrls(imageUrls);
        }

        if (outfit.getStyles() != null) {
            List<OutfitResponseDto.StyleDto> styles = outfit.getStyles().stream()
                    .map(this::toStyleDto)
                    .collect(Collectors.toList());
            dto.setStyles(styles);
        }

        if (outfit.getUser() != null) {
            dto.setUserId(outfit.getUser().getId());
            dto.setUsername(outfit.getUser().getUsername());
        }

        if (outfit.getEvent() != null) {
            dto.setEventId(outfit.getEvent().getId());
            dto.setEventName(outfit.getEvent().getName());
        }

        return dto;
    }

    public List<OutfitResponseDto> toResponseDtoList(List<Outfit> outfits) {
        if (outfits == null) {
            return List.of();
        }
        return outfits.stream()
                .map(this::toResponseDto)
                .collect(Collectors.toList());
    }

    public Outfit toEntity(OutfitRequestDto dto) {
        if (dto == null) {
            return null;
        }

        Outfit outfit = new Outfit();
        outfit.setName(dto.getName());
        outfit.setDescription(dto.getDescription());
        outfit.setGender(dto.getGender());

        return outfit;
    }

    private OutfitResponseDto.StyleDto toStyleDto(OutfitStyle style) {
        OutfitResponseDto.StyleDto dto = new OutfitResponseDto.StyleDto();
        dto.setId(style.getId());
        dto.setName(style.getName());
        dto.setDescription(style.getDescription());
        return dto;
    }
}
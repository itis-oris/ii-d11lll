package com.eventoutfit.service;

import com.eventoutfit.model.*;
import com.eventoutfit.repository.OutfitImageRepository;
import com.eventoutfit.repository.OutfitRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class OutfitService {

    private static final Logger logger = LoggerFactory.getLogger(OutfitService.class);

    private final OutfitRepository outfitRepository;
    private final OutfitImageRepository imageRepository;
    private final UserService userService;
    private final EventService eventService;
    private final OutfitStyleService styleService;

    public OutfitService(OutfitRepository outfitRepository,
                         OutfitImageRepository imageRepository,
                         UserService userService,
                         EventService eventService,
                         OutfitStyleService styleService) {
        this.outfitRepository = outfitRepository;
        this.imageRepository = imageRepository;
        this.userService = userService;
        this.eventService = eventService;
        this.styleService = styleService;
    }


    public List<Outfit> findAll() {
        logger.debug("Получение всех образов");
        List<Outfit> outfits = outfitRepository.findAll();
        logger.debug("Найдено образов: {}", outfits.size());
        return outfits;
    }

    public List<Outfit> getUserOutfits(Long userId) {
        logger.debug("Получение образов пользователя: userId={}", userId);
        List<Outfit> outfits = outfitRepository.findByUserId(userId);
        logger.debug("Найдено образов для пользователя {}: {}", userId, outfits.size());
        return outfits;
    }

    public List<Outfit> getOutfitsWithManyComments() {
        logger.debug("Подзапрос: образы с >2 комментариями");
        return outfitRepository.findOutfitsWithMoreThanTwoComments();
    }

    public Optional<Outfit> findById(Long id) {
        logger.debug("Поиск образа по ID: {}", id);
        return outfitRepository.findById(id);
    }

    public List<Outfit> getOutfitsByEventAndGender(Long eventId, String gender) {
        logger.debug("Фильтрация образов: eventId={}, gender={}", eventId, gender);

        if (!eventService.existsById(eventId)) {
            logger.warn("Мероприятие не найдено: eventId={}", eventId);
            return new ArrayList<>();
        }

        List<Outfit> outfits = outfitRepository.findByEventAndGenderSortedByDate(eventId, gender);
        logger.info("Найдено образов по фильтру: eventId={}, gender={}, count={}",
                eventId, gender, outfits.size());

        return outfits;
    }

    public List<Outfit> searchByKeyword(String keyword) {
        logger.debug("Поиск образов по ключевому слову: {}", keyword);

        if (keyword == null || keyword.trim().isEmpty()) {
            logger.warn("Пустой поисковый запрос");
            return new ArrayList<>();
        }

        List<Outfit> results = outfitRepository.searchByNameKeyword(keyword.trim());
        logger.info("Результатов поиска по ключевому слову '{}': {}", keyword, results.size());

        return results;
    }

    @Transactional
    public boolean createOutfit(String name, String description, String gender,
                                Long userId, List<String> imageUrls, Long eventId,
                                List<Long> styleIds, Double price) {
        logger.info("Создание образа: name={}, userId={}, eventId={}", name, userId, eventId);

        if (name == null || name.trim().isEmpty()) {
            logger.warn("Создание образа отклонено: пустое название");
            return false;
        }

        if (gender == null || (!gender.equals("MALE") && !gender.equals("FEMALE"))) {
            logger.warn("Создание образа отклонено: неверный пол {}", gender);
            return false;
        }

        if (userService.findById(userId).isEmpty()) {
            logger.warn("Создание образа отклонено: пользователь не найден, userId={}", userId);
            return false;
        }

        if (!eventService.existsById(eventId)) {
            logger.warn("Создание образа отклонено: мероприятие не найдено, eventId={}", eventId);
            return false;
        }

        Event event = eventService.findById(eventId).orElse(null);
        User user = userService.findById(userId).orElse(null);

        Outfit outfit = new Outfit();
        outfit.setName(name.trim());
        outfit.setDescription(description != null ? description.trim() : null);
        outfit.setGender(gender);
        outfit.setUser(user);
        outfit.setEvent(event);
        outfit.setPrice(price != null ? price : 0.0);

        Outfit savedOutfit = outfitRepository.save(outfit);
        logger.debug("Образ сохранен с ID: {}", savedOutfit.getId());

        if (styleIds != null && !styleIds.isEmpty()) {
            List<OutfitStyle> styles = styleService.findByIds(styleIds);
            savedOutfit.setStyles(styles);
            logger.debug("Добавлено {} стилей к образу", styles.size());
        }

        if (imageUrls != null && !imageUrls.isEmpty()) {
            int savedImagesCount = 0;
            for (String url : imageUrls) {
                if (url != null && !url.trim().isEmpty()) {
                    OutfitImage image = new OutfitImage();
                    image.setImageUrl(url.trim());
                    image.setOutfit(savedOutfit);
                    imageRepository.save(image);
                    savedImagesCount++;
                }
            }
            logger.info("Добавлено {} фото к образу", savedImagesCount);
        }

        logger.info("Образ успешно создан: id={}, name={}", savedOutfit.getId(), savedOutfit.getName());
        return true;
    }


    @Transactional
    public boolean updateOutfit(Long id, String name, String description,
                                String gender, Long eventId, List<Long> styleIds) {
        logger.info("Обновление образа: id={}, name={}", id, name);

        Optional<Outfit> outfitOpt = outfitRepository.findById(id);
        if (outfitOpt.isEmpty()) {
            logger.warn("Обновление отклонено: образ не найден, id={}", id);
            return false;
        }

        Outfit outfit = outfitOpt.get();

        if (eventId != null && eventService.existsById(eventId)) {
            Optional<Event> event = eventService.findById(eventId);
            event.ifPresent(outfit::setEvent);
        }

        if (name != null && !name.trim().isEmpty()) {
            outfit.setName(name.trim());
        }

        if (description != null && !description.trim().isEmpty()) {
            outfit.setDescription(description.trim());
        }

        if (gender != null && (gender.equals("MALE") || gender.equals("FEMALE"))) {
            outfit.setGender(gender);
        }

        if (styleIds != null) {
            List<OutfitStyle> styles = styleService.findByIds(styleIds);
            outfit.setStyles(styles);
            logger.debug("Обновлены стили образа: {}", styles.size());
        }

        outfitRepository.save(outfit);
        logger.info("Образ успешно обновлен: id={}", id);
        return true;
    }

    @Transactional
    public boolean addImagesToOutfit(Long outfitId, List<String> imageUrls) {
        logger.info("Добавление фото к образу: outfitId={}", outfitId);

        Optional<Outfit> outfit = outfitRepository.findById(outfitId);
        if (outfit.isEmpty()) {
            logger.warn("Добавление фото отклонено: образ не найден, outfitId={}", outfitId);
            return false;
        }

        if (imageUrls == null || imageUrls.isEmpty()) {
            logger.warn("Добавление фото отклонено: пустой список URL");
            return false;
        }

        int savedCount = 0;
        for (String url : imageUrls) {
            if (url != null && !url.trim().isEmpty()) {
                OutfitImage image = new OutfitImage();
                image.setImageUrl(url.trim());
                image.setOutfit(outfit.get());
                imageRepository.save(image);
                savedCount++;
            }
        }

        logger.info("Добавлено {} фото к образу {}", savedCount, outfitId);
        return savedCount > 0;
    }


    @Transactional
    public boolean deleteOutfit(Long id) {
        logger.info("Удаление образа: id={}", id);

        if (!outfitRepository.existsById(id)) {
            logger.warn("Удаление отклонено: образ не найден, id={}", id);
            return false;
        }

        outfitRepository.deleteById(id);
        logger.info("Образ успешно удален: id={}", id);
        return true;
    }

    @Transactional
    public boolean deleteImage(Long imageId) {
        logger.info("Удаление изображения: imageId={}", imageId);

        if (!imageRepository.existsById(imageId)) {
            logger.warn("Удаление отклонено: изображение не найдено, imageId={}", imageId);
            return false;
        }

        imageRepository.deleteById(imageId);
        logger.info("Изображение успешно удалено: imageId={}", imageId);
        return true;
    }
}
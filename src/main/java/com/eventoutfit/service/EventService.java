package com.eventoutfit.service;

import com.eventoutfit.model.Event;
import com.eventoutfit.repository.EventRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventService {
    private static final Logger logger = LoggerFactory.getLogger(EventService.class);

    private final EventRepository eventRepository;

    public EventService(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
    }

    @Cacheable(value = "events")
    public List<Event> getAllEvents() {
        logger.debug("Запрос всех мероприятий (из БД, не из кэша)");
        List<Event> events = eventRepository.findAll();
        logger.debug("Найдено мероприятий: {}", events.size());
        return events;
    }

    public Optional<Event> findById(Long id) {
        logger.debug("Поиск мероприятия по ID: {}", id);
        return eventRepository.findById(id);
    }

    public boolean existsById(Long id) {
        logger.debug("Проверка существования мероприятия: id={}", id);
        return eventRepository.existsById(id);
    }
}
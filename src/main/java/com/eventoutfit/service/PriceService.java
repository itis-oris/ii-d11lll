package com.eventoutfit.service;

import com.eventoutfit.model.Outfit;
import com.eventoutfit.util.ApiClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class PriceService {

    private static final Logger logger = LoggerFactory.getLogger(PriceService.class);

    @Autowired
    private ApiClient apiClient;

    public Map<String, String> getPriceInCurrencies(Outfit outfit) {
        Map<String, String> result = new HashMap<>();

        if (outfit.getPrice() == null) {
            result.put("error", "Цена не указана");
            return result;
        }

        double rub = outfit.getPrice();

        try {
            double usd = apiClient.convertRubToCurrency(rub, "USD");
            double eur = apiClient.convertRubToCurrency(rub, "EUR");

            result.put("RUB", String.format("%.0f ₽", rub));
            result.put("USD", String.format("%.2f $", usd));
            result.put("EUR", String.format("%.2f €", eur));

            logger.info("Цены для {}: RUB={}, USD={}, EUR={}",
                    outfit.getName(), rub, usd, eur);

        } catch (Exception e) {
            logger.error("Ошибка конвертации валют", e);
            result.put("error", "Ошибка получения курсов");
        }

        return result;
    }
}
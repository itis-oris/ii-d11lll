package com.eventoutfit.controller;

import com.eventoutfit.model.Outfit;
import com.eventoutfit.service.OutfitService;
import com.eventoutfit.util.ApiClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/currency")
public class CurrencyRestController {

    private static final Logger logger = LoggerFactory.getLogger(CurrencyRestController.class);
    private final ApiClient currencyApiClient;
    private final OutfitService outfitService;

    public CurrencyRestController(ApiClient currencyApiClient, OutfitService outfitService) {
        this.currencyApiClient = currencyApiClient;
        this.outfitService = outfitService;
    }

    @GetMapping("/convert")
    public ResponseEntity<Map<String, Object>> convertPrice(
            @RequestParam Long outfitId,
            @RequestParam String currency) {

        logger.debug("Конвертация цены: outfitId={}, currency={}", outfitId, currency);

        Outfit outfit = outfitService.findById(outfitId).orElse(null);
        if (outfit == null) {
            return ResponseEntity.notFound().build();
        }

        double convertedPrice = currencyApiClient.convertRubToCurrency(outfit.getPrice(), currency);
        Map<String, Double> rates = currencyApiClient.getExchangeRates();

        Map<String, Object> response = new HashMap<>();
        response.put("originalPrice", outfit.getPrice());
        response.put("originalCurrency", "RUB");
        response.put("convertedPrice", convertedPrice);
        response.put("targetCurrency", currency.toUpperCase());
        response.put("exchangeRate", rates.get(currency.toUpperCase()));

        return ResponseEntity.ok(response);
    }

    @GetMapping("/rates")
    public ResponseEntity<Map<String, Double>> getRates() {
        return ResponseEntity.ok(currencyApiClient.getExchangeRates());
    }
}
package com.eventoutfit.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
public class ApiClient {

    private static final Logger logger = LoggerFactory.getLogger(ApiClient.class);
    private final OkHttpClient httpClient;
    private final ObjectMapper objectMapper;

    @Value("${currency.api.url:https://api.exchangerate-api.com/v4/latest/}")
    private String apiUrl;

    public ApiClient() {
        this.httpClient = new OkHttpClient();
        this.objectMapper = new ObjectMapper();
    }

    public Map<String, Double> getExchangeRates() {
        logger.debug("Запрос курсов валют");

        try {
            String url = apiUrl + "RUB";
            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .build();

            try (Response response = httpClient.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    throw new IOException("API вернул ошибку: " + response.code());
                }

                String json = response.body().string();
                JsonNode root = objectMapper.readTree(json);
                JsonNode rates = root.path("rates");

                Map<String, Double> result = new HashMap<>();
                result.put("USD", rates.path("USD").asDouble());
                result.put("EUR", rates.path("EUR").asDouble());
                result.put("CNY", rates.path("CNY").asDouble());

                logger.info("Курсы: USD={}, EUR={}, CNY={}",
                        result.get("USD"), result.get("EUR"), result.get("CNY"));
                return result;
            }
        } catch (IOException e) {
            logger.error("Ошибка получения курсов: {}", e.getMessage());

            Map<String, Double> fallback = new HashMap<>();
            fallback.put("USD", 0.011);
            fallback.put("EUR", 0.010);
            fallback.put("CNY", 0.079);
            return fallback;
        }
    }

    public double convertRubToCurrency(double rubAmount, String currencyCode) {
        Map<String, Double> rates = getExchangeRates();
        Double rate = rates.get(currencyCode.toUpperCase());

        if (rate == null) {
            logger.warn("Валюта {} не найдена", currencyCode);
            return rubAmount * 0.011;
        }

        return rubAmount * rate;
    }
}
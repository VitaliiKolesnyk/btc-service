package com.api.client;

import ch.qos.logback.classic.Logger;
import com.api.model.ApiResponse;
import com.api.service.EmailService;
import com.api.service.impl.FileServiceImpl;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class CoinApiClient {
    private static final Logger log = (Logger) LoggerFactory.getLogger(CoinApiClient.class);

    private final RestTemplate restTemplate;

    private ApiResponse originalApiResponse = new ApiResponse();

    @Value("${coinapi.base-url}")
    private String baseUrl;

    @Value("${coinapi.api-key}")
    private String apiKey;

    public ApiResponse getBitcoinPrice() {
        String apiUrl = baseUrl + "/v1/exchangerate/BTC/UAH";
        HttpHeaders headers = new HttpHeaders();
        headers.set("X-CoinAPI-Key", apiKey);

        HttpEntity<String> entity = new HttpEntity<>(headers);

        log.info("Sending request to {}", apiUrl);

        ApiResponse apiResponse = restTemplate.exchange(apiUrl, HttpMethod.GET, entity, ApiResponse.class).getBody();

        log.info("Response with BTC to UAH rate {} received", apiResponse.getRate());

        return apiResponse;
    }

    public void updateOriginalApiResponse(ApiResponse newApiResponse) {
        if (originalApiResponse.getRate() == null || !originalApiResponse.getRate().equals(newApiResponse.getRate())) {
            originalApiResponse.setRate(newApiResponse.getRate());
        }
    }

    public ApiResponse getOriginalApiResponse() {
        return originalApiResponse;
    }
}

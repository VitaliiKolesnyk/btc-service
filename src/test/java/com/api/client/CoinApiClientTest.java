package com.api.client;

import com.api.mapper.ApiResponseToExchangeRateMapper;
import com.api.model.ApiResponse;
import com.api.service.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;

class CoinApiClientTest {
    @Mock
    private RestTemplate restTemplate;

    private CoinApiClient coinApiClient;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        coinApiClient = new CoinApiClient(restTemplate);
    }

    @Test
    void getBitcoinPrice_ShouldReturnApiResponse() {
        // Mock response
        ApiResponse expectedApiResponse = new ApiResponse();
        Mockito.when(restTemplate.exchange(anyString(), Mockito.eq(HttpMethod.GET), Mockito.any(HttpEntity.class), Mockito.eq(ApiResponse.class)))
                .thenReturn(ResponseEntity.ok(expectedApiResponse));

        // Invoke the method
        ApiResponse actualApiResponse = coinApiClient.getBitcoinPrice();

        // Verify the interaction
        verify(restTemplate).exchange(anyString(), Mockito.eq(HttpMethod.GET), Mockito.any(HttpEntity.class), Mockito.eq(ApiResponse.class));

        // Assert the result
        assertEquals(expectedApiResponse, actualApiResponse);
    }
}
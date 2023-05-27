package com.api.mapper;

import com.api.model.ApiResponse;
import com.api.model.ExchangeRate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ApiResponseToExchangeRateMapperTest {

    private ApiResponseToExchangeRateMapper mapper;
    private ApiResponse apiResponse;

    @BeforeEach
    void setUp() {
        mapper = new ApiResponseToExchangeRateMapper();
        apiResponse = new ApiResponse(new BigDecimal("10"));
    }

    @Test
    void map_shouldReturnExchangeRateWithCorrectValues() {
        ExchangeRate exchangeRate = mapper.map(apiResponse);

        assertEquals("BTC", exchangeRate.getFrom());
        assertEquals("UAH", exchangeRate.getTo());
        assertEquals(new BigDecimal("10"), exchangeRate.getRate());
    }
}
package com.api.mapper;

import com.api.model.ApiResponse;
import com.api.model.ExchangeRate;
import org.springframework.stereotype.Component;

@Component
public class ApiResponseToExchangeRateMapper {

    public ExchangeRate map(ApiResponse apiResponse) {
        return new ExchangeRate("BTC", "UAH", apiResponse.getRate());
    }
}

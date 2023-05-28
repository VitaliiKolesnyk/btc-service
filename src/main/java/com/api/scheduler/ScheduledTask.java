package com.api.scheduler;

import com.api.client.CoinApiClient;
import com.api.mapper.ApiResponseToExchangeRateMapper;
import com.api.model.ApiResponse;
import com.api.model.Subscriber;
import com.api.service.EmailService;
import com.api.service.FileService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class ScheduledTask {

    private final CoinApiClient coinApiClient;

    private final EmailService emailService;

    private final FileService fileService;

    private final ApiResponseToExchangeRateMapper mapper;

    public ScheduledTask(CoinApiClient coinApiClient, EmailService emailService, FileService fileService, ApiResponseToExchangeRateMapper mapper) {
        this.coinApiClient = coinApiClient;
        this.emailService = emailService;
        this.fileService = fileService;
        this.mapper = mapper;
    }

    @Scheduled(cron = "0 0/10 * * * ?")
    public void performScheduledTask() throws IOException {
        ApiResponse apiResponse = coinApiClient.getBitcoinPrice();

        ApiResponse originalApiResponse = coinApiClient.getOriginalApiResponse();

        if (originalApiResponse != null && !apiResponse.equals(originalApiResponse)) {
            List<Subscriber> subscribers = fileService.getSubscribers();

            emailService.sendAll(subscribers, mapper.map(apiResponse));
        }

        coinApiClient.updateOriginalApiResponse(apiResponse);
    }
}

package com.api.controller;

import ch.qos.logback.core.pattern.color.ANSIConstants;
import com.api.client.CoinApiClient;
import com.api.mapper.ApiResponseToExchangeRateMapper;
import com.api.model.ApiResponse;
import com.api.model.Subscriber;
import com.api.service.EmailService;
import com.api.service.FileService;
import com.api.util.EmailValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;
import java.util.Collections;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

class BtcControllerTest {
    private MockMvc mockMvc;

    @Mock
    private CoinApiClient coinApiClient;

    @Mock
    private FileService fileService;

    @Mock
    private EmailService emailService;

    @Mock
    private ApiResponseToExchangeRateMapper mapper;

    @Mock
    private EmailValidator validator;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(new BtcController(coinApiClient, fileService, emailService, mapper, validator)).build();
    }

    @Test
    void getBtcToUahRateTest() throws Exception {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setRate(new BigDecimal(10));

        when(coinApiClient.getBitcoinPrice()).thenReturn(apiResponse);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/rate")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.rate").value(10.0));
    }

    @Test
    void subscribeTest() throws Exception {
        String email = "test@example.com";

        doNothing().when(validator).validate(any(String.class));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/subscription")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                        .param("email", email))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Email was succesfully added to subscription list"));
    }

    @Test
    void sendEmailsTest() throws Exception {
        ApiResponse apiResponse = new ApiResponse();
        apiResponse.setRate(new BigDecimal(10));

        when(coinApiClient.getBitcoinPrice()).thenReturn(apiResponse);
        when(fileService.getSubscribers()).thenReturn(Collections.singletonList(new Subscriber("test@example.com")));

        mockMvc.perform(MockMvcRequestBuilders.post("/api/sendEmails")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().string("Emails were succesfully sent"));

    }
}
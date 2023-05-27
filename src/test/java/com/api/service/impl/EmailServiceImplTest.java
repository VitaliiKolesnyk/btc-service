package com.api.service.impl;

import com.api.model.ExchangeRate;
import com.api.model.Subscriber;
import com.api.service.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;

class EmailServiceImplTest {

    @Mock
    private JavaMailSender mailSender;

    private EmailService emailService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        emailService = new EmailServiceImpl(mailSender);
    }

    @Test
    void sendShouldSendEmailToSubscriber() {
        Subscriber subscriber = new Subscriber("test@example.com");
        ExchangeRate exchangeRate = new ExchangeRate("BTC", "UAH", new BigDecimal(5000));

        emailService.send(subscriber, exchangeRate);

        verify(mailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    void sendAllShouldSendEmailsToAllSubscribers() {
        List<Subscriber> subscribers = Arrays.asList(
                new Subscriber("test1@example.com"),
                new Subscriber("test2@example.com"),
                new Subscriber("test3@example.com")
        );
        ExchangeRate exchangeRate = new ExchangeRate("BTC", "UAH", new BigDecimal(5000));

        emailService.sendAll(subscribers, exchangeRate);

        verify(mailSender, times(3)).send(any(SimpleMailMessage.class));
    }
}
package com.api.service.impl;

import ch.qos.logback.classic.Logger;
import com.api.model.ExchangeRate;
import com.api.model.Subscriber;
import com.api.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private static final Logger log = (Logger) LoggerFactory.getLogger(EmailServiceImpl.class);

    private final JavaMailSender mailSender;

    @Override
    public void send(Subscriber subscriber, ExchangeRate exchangeRate) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(subscriber.getMail());
        message.setSubject("BTC to EUR rate");
        message.setText("Dear Subscriber,\n\n" +
                "Actual BTC to UAH rate is - " + exchangeRate.getRate() + "\n\n" +
                "Thank you.");

        log.info("Sending email to {}", subscriber.getMail());
        mailSender.send(message);
    }

    public void sendAll(List<Subscriber> subscribers, ExchangeRate exchangeRate) {
        subscribers.forEach(subscriber -> send(subscriber, exchangeRate));
    }
}

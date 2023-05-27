package com.api.service;

import com.api.model.ExchangeRate;
import com.api.model.Subscriber;

import java.util.List;

public interface EmailService {

    void send(Subscriber subscriber, ExchangeRate exchangeRate);
    void sendAll(List<Subscriber> subscribers, ExchangeRate exchangeRate);
}

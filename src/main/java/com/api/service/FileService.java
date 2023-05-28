package com.api.service;

import com.api.exception.SubscriberAlreadySubscribedException;
import com.api.model.Subscriber;

import java.io.IOException;
import java.util.List;

public interface FileService {

    void addSubscriber(Subscriber subscriber) throws SubscriberAlreadySubscribedException, IOException;

    List<Subscriber> getSubscribers() throws IOException;
}

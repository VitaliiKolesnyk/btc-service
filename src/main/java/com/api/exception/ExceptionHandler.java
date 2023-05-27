package com.api.exception;

import ch.qos.logback.classic.Logger;
import com.api.client.CoinApiClient;
import com.api.model.Response;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;

@ControllerAdvice
public class ExceptionHandler {
    private static final Logger log = (Logger) LoggerFactory.getLogger(ExceptionHandler.class);

    @org.springframework.web.bind.annotation.ExceptionHandler(SubscriberAlreadySubscribedException.class)
    public ResponseEntity<String> handleSubscriberException(SubscriberAlreadySubscribedException e) {
        return ResponseEntity.status(HttpStatus.CONFLICT).body(Response.ALREADY_SUBSCRIBED.getResponse());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler(NotValidEmailException.class)
    public ResponseEntity<String> handleNotValidEmailException(NotValidEmailException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Response.NOT_VALID_EMAIL.getResponse());
    }

    @org.springframework.web.bind.annotation.ExceptionHandler()
    public ResponseEntity<String> handleExceptions(Exception e) {
        log.error("Exception occured: {}", e.getStackTrace());

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Response.ERROR.getResponse());
    }
}

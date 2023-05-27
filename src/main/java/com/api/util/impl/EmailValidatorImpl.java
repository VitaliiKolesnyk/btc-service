package com.api.util.impl;

import ch.qos.logback.classic.Logger;
import com.api.exception.NotValidEmailException;
import com.api.service.impl.EmailServiceImpl;
import com.api.util.EmailValidator;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class EmailValidatorImpl implements EmailValidator {
    private static final Logger log = (Logger) LoggerFactory.getLogger(EmailServiceImpl.class);

    @Override
    public void validate(String email) {
        String emailPattern = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$";

        if (!email.matches(emailPattern)) {
            log.warn("Email {} is not valid", email);
            throw new NotValidEmailException();
        }
    }
}

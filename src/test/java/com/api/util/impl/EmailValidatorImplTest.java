package com.api.util.impl;

import com.api.exception.NotValidEmailException;
import com.api.util.EmailValidator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EmailValidatorImplTest {
    private final EmailValidator emailValidator = new EmailValidatorImpl();

    @Test
    void validate_ValidEmail_DoesNotThrowException() {
        String email = "test@example.com";

        assertDoesNotThrow(() -> emailValidator.validate(email));
    }

    @Test
    void validate_InvalidEmail_ThrowsNotValidEmailException() {
        String email = "invalid-email";

        assertThrows(NotValidEmailException.class, () -> emailValidator.validate(email));
    }
}
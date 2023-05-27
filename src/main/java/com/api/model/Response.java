package com.api.model;

public enum Response {
    EMAIL_ADDED("Email was succesfully added to subscription list"),
    EMAILS_SENT("Emails were succesfully sent"),
    ALREADY_SUBSCRIBED("Email already present in subscription list"),
    NOT_VALID_EMAIL("Email is not valid"),
    ERROR("Unpredictable exception");

    String response;

    Response(String response) {
        this.response = response;
    }

    public String getResponse() {
        return response;
    }
}

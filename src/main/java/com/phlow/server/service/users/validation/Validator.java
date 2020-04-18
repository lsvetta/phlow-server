package com.phlow.server.service.users.validation;

public interface Validator {
    boolean validatePassword(String input);
    boolean validateEmail(String input);
    boolean validateUsername(String input);
}

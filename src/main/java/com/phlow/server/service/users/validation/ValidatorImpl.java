package com.phlow.server.service.users.validation;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ValidatorImpl implements Validator {

    private Pattern passwordPattern;
    private Pattern emailPattern;
    private Pattern usernamePattern;

    @Value("${phlow.security.passwordRegex}")
    private String PASSWORD_PATTERN;
    @Value("${phlow.security.emailRegex}")
    private String EMAIL_PATTERN;
    @Value("${phlow.security.usernameRegex}")
    private String USERNAME_PATTERN;

    @PostConstruct
    public void init() {
        this.passwordPattern = Pattern.compile(PASSWORD_PATTERN);
        this.emailPattern = Pattern.compile(EMAIL_PATTERN, Pattern.CASE_INSENSITIVE);
        this.usernamePattern = Pattern.compile(USERNAME_PATTERN, Pattern.CASE_INSENSITIVE);
    }

    @Override
    public boolean validatePassword(final String password) {
        Matcher matcher = passwordPattern.matcher(password);
        return matcher.matches();
    }

    @Override
    public boolean validateEmail(String email) {
        Matcher matcher = emailPattern.matcher(email);
        return matcher.matches();
    }

    @Override
    public boolean validateUsername(String username) {
        Matcher matcher = usernamePattern.matcher(username);
        return matcher.matches();
    }
}

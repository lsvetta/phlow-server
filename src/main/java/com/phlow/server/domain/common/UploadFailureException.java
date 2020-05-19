package com.phlow.server.domain.common;

public class UploadFailureException extends RuntimeException {
    public UploadFailureException(String message) {
        super(message);
    }
}

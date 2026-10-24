package com.interview.exception;

/** Extend for "request is invalid on its own" errors → 400. */
public class BadRequestException extends RuntimeException {
    public BadRequestException(String message) {
        super(message);
    }
}

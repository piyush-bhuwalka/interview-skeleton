package com.interview.exception;

/** Extend for "resource doesn't exist" errors → 404. */
public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}

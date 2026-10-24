package com.interview.exception;

/** Extend for "valid request, but conflicts with current state" errors → 409. */
public class ConflictException extends RuntimeException {
    public ConflictException(String message) {
        super(message);
    }
}

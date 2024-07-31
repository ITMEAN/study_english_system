package org.minh.testservice.exception;

public class BadCredentialsException extends Exception {
    public BadCredentialsException(String msg) {
        super(msg);
    }
}

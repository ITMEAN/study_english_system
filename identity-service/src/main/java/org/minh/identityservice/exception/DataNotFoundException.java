package org.minh.identityservice.exception;

public class DataNotFoundException extends RuntimeException {
    public DataNotFoundException(String msg){
        super(msg);
    }
}

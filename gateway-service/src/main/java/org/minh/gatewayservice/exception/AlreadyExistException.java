package org.minh.gatewayservice.exception;
public class AlreadyExistException extends RuntimeException{
    public AlreadyExistException(String msg){
        super(msg);
    }
}

package org.minh.testservice.exception;

public class S3UploadException extends Exception{
    public S3UploadException(String msg){
        super(msg);
    }
}

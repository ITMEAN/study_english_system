package org.minh.flashcardservice.exception;

import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

@Component
public class CustomErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String s, Response response) {
        HttpStatus status = HttpStatus.valueOf(response.status());
        switch (status) {
            case NOT_FOUND:
                return new DataNotFoundException("Data not found");
            case UNAUTHORIZED:
                return new BadCredentialsException("Bad credentials");
            case INTERNAL_SERVER_ERROR:
                return new S3UploadException("S3 upload error");
            default:
                return new Exception("Unknown error");
        }
    }
}

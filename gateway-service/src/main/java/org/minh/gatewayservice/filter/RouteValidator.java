package org.minh.gatewayservice.filter;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.function.Predicate;

@Component
@RequiredArgsConstructor
public class RouteValidator {
    @Value("${api.prefix}")
    private  String prefix;
    public  List<String> openApiEndpoints = List.of(
            "api/v1/auth/register",
            "api/v1/auth/login",
            "api/v1/auth/refreshToken",
            "api/v1/auth/validate-otp-register",
            "api/v1/auth/forgot-password/",
            "api/v1/auth/reset-password",
            "api/v1/auth/resend-otp",
            "api/v1/tests/",
            "api/v1/exercise/flashcard/user/"

            );

    public Predicate<ServerHttpRequest> isSecured =
            request -> openApiEndpoints
                    .stream()
                    .noneMatch(uri -> request.getURI().getPath().contains(uri));

}

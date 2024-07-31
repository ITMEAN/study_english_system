package org.minh.gatewayservice.filter;

import lombok.RequiredArgsConstructor;
import org.minh.gatewayservice.entity.Users;
import org.minh.gatewayservice.jwt.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;

@Component
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {
    @Autowired
    private RouteValidator validator;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private  UserDetailsService userDetailsService;
    public AuthenticationFilter() {
        super(Config.class);
    }
    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange,chain)->{
            System.out.println(validator.isSecured.test(exchange.getRequest()));
            if (validator.isSecured.test(exchange.getRequest())){
                //header contains token or not
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)){
                    throw new BadCredentialsException("missing authorization header");
                }
                // get token from header
                String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
                if(authHeader != null && authHeader.startsWith("Bearer ")){
                    authHeader = authHeader.substring(7);
                }
                String subject = jwtUtil.getSubject(authHeader);
                Users userDetails = (Users) userDetailsService.loadUserByUsername(subject);
                try{
                    boolean isValidToken = jwtUtil.validateToken(authHeader,userDetails);
                    if(isValidToken){
                        return chain.filter(exchange);
                    }else{
                        throw new BadCredentialsException("invalid token");
                    }
                }catch (Exception e){
                    throw new BadCredentialsException(e.getMessage());
                }
            }
            return chain.filter(exchange);
        });
    }


    public static class Config {

    }
}

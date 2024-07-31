package org.minh.identityservice.config;

import lombok.RequiredArgsConstructor;
import org.minh.identityservice.filter.JwtTokenFilter;
import org.minh.identityservice.service.auth.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.boot.actuate.autoconfigure.security.reactive.EndpointRequest;



@Configuration
//@EnableMethodSecurity
@EnableWebSecurity(debug = true)
@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableWebMvc
@RequiredArgsConstructor

public class WebSecurityConfig {
    private final JwtTokenFilter jwtTokenFilter;
    @Value("${api.prefix}")
    private String apiPrefix;



    @Bean
    public AuthenticationSuccessHandle authenticationSuccessHandle(){
        return new AuthenticationSuccessHandle();
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http)  throws Exception{
        http
                .authorizeHttpRequests(requests -> {
                    requests
                            .anyRequest().permitAll();
                })
                .csrf(AbstractHttpConfigurer::disable);

        //oauth config
        http.oauth2Login(Customizer.withDefaults()).oauth2Login(
                oauth2 ->
                        oauth2
                                .loginPage("/oauth2/authorization/google")
                                .successHandler(authenticationSuccessHandle())
        );

        return http.build();
    }


}

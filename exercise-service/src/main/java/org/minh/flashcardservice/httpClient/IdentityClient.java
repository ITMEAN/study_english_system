package org.minh.flashcardservice.httpClient;


import org.minh.flashcardservice.dto.UserDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.*;

@Component
@FeignClient (name = "identity-service")
public interface IdentityClient {
    @PostMapping(value = "/api/v1/auth/users/is-exist",
            produces = MediaType.APPLICATION_JSON_VALUE
    )
    boolean isExistUser(@RequestBody String username, @RequestHeader("Authorization") String token);

    @GetMapping(value = "/api/v1/auth/users/{email}",produces = MediaType.APPLICATION_JSON_VALUE)
    UserDTO getUserByEmail(@PathVariable String email, @RequestHeader("Authorization") String token);


}

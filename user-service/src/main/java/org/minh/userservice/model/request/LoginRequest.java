package org.minh.userservice.model.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginRequest {
    @NotBlank(message = "email must not be blank")
    @NotNull(message = "email must not be null")
    private String email;
    @NotBlank(message = "password must not be blank")
    @NotNull (message = "password must not be null")
    private String password;
}

package org.minh.userservice.model.request;


import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreateUserRequest {
    @Size(min = 8,message = "password must be at least 8 character")
    private String password;
    private String repassword;
    @NotEmpty(message = "firstName must not be empty")
    @NotNull(message = "firstName must not be null")
    private String firstName;
    @NotEmpty(message = "lastname must not be empty")
    @NotNull(message = "lastname must not be null")
    private String lastName;
    @NotNull(message = "Email not be null")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$", message = "Invalid email format")
    private String email;
    private String phone;
    private LocalDate lob;
}

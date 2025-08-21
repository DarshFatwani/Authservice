package org.example.model;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.example.validation.ValidPassword;
@Data
public class UserInfoDto {

    @NotBlank
    @Size(min = 3, max = 50)
    private String username;

    @NotBlank
    private String firstName;

    private String lastName;

    private Long phoneNumber;

    @NotBlank
    @Email
    @Size(max = 255)
    private String email;

    @NotBlank
    @ValidPassword
    private String password;

    // getters and setters
}

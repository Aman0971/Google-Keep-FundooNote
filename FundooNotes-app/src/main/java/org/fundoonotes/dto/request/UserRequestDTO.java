package org.fundoonotes.dto.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

    @Data
    public class UserRequestDTO {

        @NotBlank(message = "First Name is required")
        private String firstName;

        @NotBlank(message = "Last Name is required")
        private String lastName;

        @Email(message = "Invalid Email")
        private String email;

        @NotBlank(message = "Password is required")
        private String password;
    }

package org.fundoonotes.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResetPasswordRequestDTO {

    private String email;

    private String otp;

    private String newPassword;

}
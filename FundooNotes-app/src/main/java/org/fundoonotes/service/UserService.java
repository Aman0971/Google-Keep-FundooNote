package org.fundoonotes.service;

import org.fundoonotes.dto.request.*;
import org.fundoonotes.dto.response.LoginResponseDTO;
import org.fundoonotes.dto.response.UserResponseDTO;

public interface UserService {
    UserResponseDTO register(UserRequestDTO requestDto);

    String verifyOtp(VerifyOtpRequestDTO requestDTO);

    LoginResponseDTO login(LoginRequestDTO requestDto);

    String forgotPassword(ForgotPasswordRequestDTO dto);

    String resetPassword(ResetPasswordRequestDTO dto);
}
//    UserResponseDTO getUserById(Long id);
//
//    List<UserResponseDTO> getAllUsers();
//
//    void deleteUser(Long id);
//}

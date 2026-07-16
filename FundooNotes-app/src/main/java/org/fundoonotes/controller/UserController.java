package org.fundoonotes.controller;

import jakarta.validation.Valid;
import org.fundoonotes.dto.request.*;
import org.fundoonotes.dto.response.LoginResponseDTO;
import org.fundoonotes.dto.response.UserResponseDTO;
import org.fundoonotes.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> registerUser(@Valid @RequestBody UserRequestDTO requestDto) {
        UserResponseDTO register = userService.register(requestDto);
        return new ResponseEntity<>(register, HttpStatus.CREATED);
    }

    @PostMapping("/verify")
    public ResponseEntity<String> verifyOtp(@RequestBody VerifyOtpRequestDTO requestDTO){
        return ResponseEntity.ok(userService.verifyOtp(requestDTO)
        );
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponseDTO> login(@Valid @RequestBody LoginRequestDTO requestDto){
        return ResponseEntity.ok(userService.login(requestDto)
        );
    }

    @GetMapping("/profile")
    public ResponseEntity<String> profile() {
        return ResponseEntity.ok("JWT Authentication Successful");
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody ForgotPasswordRequestDTO dto){
        return ResponseEntity.ok(userService.forgotPassword(dto)
        );
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequestDTO dto){
        return ResponseEntity.ok(userService.resetPassword(dto)
        );
    }
}


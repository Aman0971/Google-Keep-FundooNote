package org.fundoonotes.service;

import org.fundoonotes.dto.request.*;
import org.fundoonotes.dto.response.UserResponseDTO;
import org.fundoonotes.messaging.EmailProducer;
import org.fundoonotes.model.User;
import org.fundoonotes.repository.UserRepository;
import org.fundoonotes.security.JwtTokenProvider;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.fundoonotes.dto.response.LoginResponseDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.time.LocalDateTime;
import java.util.Random;

//import lombok.AllArgsConstructor;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import lombok.Setter;
//
//@NoArgsConstructor
//@AllArgsConstructor
//@Getter
//@Setter

@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final EmailProducer emailProducer;



    public UserServiceImpl(UserRepository userRepository, EmailService emailService, PasswordEncoder passwordEncoder,
                           JwtTokenProvider jwtTokenProvider,EmailProducer emailProducer) {

        this.userRepository = userRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
        this.emailProducer = emailProducer;

    }

    @Override
    public UserResponseDTO register(UserRequestDTO requestDto) {

        if(userRepository.existsByEmail(requestDto.getEmail())){
            throw new RuntimeException("Email already exists");
        }

        User user = new User();

        user.setFirstName(requestDto.getFirstName());
        user.setLastName(requestDto.getLastName());
        user.setEmail(requestDto.getEmail());
        user.setPassword(passwordEncoder.encode(requestDto.getPassword()));

        user.setVerified(true);

//       // OTP Generate
//        String otp = String.format("%06d", new Random().nextInt(999999));
//        user.setOtp(otp);
//
//        // OTP 5 minute ke liye valid
//        user.setOtpExpiry(LocalDateTime.now().plusMinutes(5));

        User savedUser = userRepository.save(user);
//
//        System.out.println("Generated OTP : " + otp);
//
//        emailProducer.sendEmail(
//                user.getEmail(),
//                otp
//        );

        return new UserResponseDTO(
                savedUser.getId(),
                savedUser.getFirstName(),
                savedUser.getLastName(),
                savedUser.getEmail()
        );
    }

//    @Override
//    public String verifyOtp(VerifyOtpRequestDTO requestDTO) {
//
//        User user = userRepository.findByEmail(requestDTO.getEmail())
//                .orElseThrow(() -> new RuntimeException("User Not Found"));
//
//        if (!user.getOtp().equals(requestDTO.getOtp())) {
//            throw new RuntimeException("Invalid OTP");
//        }
//        if (user.getOtpExpiry().isBefore(LocalDateTime.now())) {
//            throw new RuntimeException("OTP Expired");
//        }
//
//        user.setVerified(true);
//        user.setOtp(null);
//        user.setOtpExpiry(null);
//
//        userRepository.save(user);
//
//        return "Email Verified Successfully";
//    }

    @Override
    public LoginResponseDTO login(LoginRequestDTO requestDto) {

        User user = userRepository.findByEmail(requestDto.getEmail())
                .orElseThrow(() -> new RuntimeException("Invalid Email"));

        if (!user.getVerified()) {
            throw new RuntimeException("Please verify your email first.");
        }

        if (!passwordEncoder.matches(requestDto.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid Password");
        }
//        return new LoginResponseDTO(
//                user.getId(),
//                user.getFirstName(),
//                user.getLastName(),
//                user.getEmail()
//        );
        String token = jwtTokenProvider.generateToken(user.getEmail());
        return new LoginResponseDTO(
                token,
                "Login Successful" ,
                user.getFirstName(),
                user.getLastName(),
                user.getEmail()
        );
    }
    @Override
    public String forgotPassword(ForgotPasswordRequestDTO dto) {

        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String otp = String.valueOf(
                (int)((Math.random()*900000)+100000));

        user.setOtp(otp);

        user.setOtpExpiry(LocalDateTime.now().plusMinutes(5));

        userRepository.save(user);

        emailProducer.sendEmail(user.getEmail(), otp);

        return "OTP Sent Successfully";
    }

    @Override
    public String resetPassword(ResetPasswordRequestDTO dto) {

        User user = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        if(user.getOtp()==null ||
                !user.getOtp().equals(dto.getOtp())){

            throw new RuntimeException("Invalid OTP");
        }

        if(user.getOtpExpiry().isBefore(LocalDateTime.now())){

            throw new RuntimeException("OTP Expired");
        }

        user.setPassword(
                passwordEncoder.encode(dto.getNewPassword())
        );

        user.setOtp(null);

        user.setOtpExpiry(null);

        userRepository.save(user);

        return "Password Reset Successfully";
    }
}

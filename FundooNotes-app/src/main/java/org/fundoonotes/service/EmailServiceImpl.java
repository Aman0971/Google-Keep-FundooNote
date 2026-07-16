package org.fundoonotes.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

    @Service
    public class EmailServiceImpl implements EmailService {

        @Autowired
        private JavaMailSender mailSender;

        @Override
        public void sendOtp(String to, String otp) {

            try {

                System.out.println("Sending mail to : " + to);

                SimpleMailMessage message = new SimpleMailMessage();

                message.setTo(to);
                message.setSubject("Fundoo Notes OTP Verification");
                message.setText("Your OTP is : " + otp);

                mailSender.send(message);

                System.out.println("Mail Sent Successfully");

            } catch (Exception e) {

                System.out.println("Mail Sending Failed");
                e.printStackTrace();

            }
        }
    }
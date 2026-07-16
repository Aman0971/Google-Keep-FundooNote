package org.fundoonotes.messaging;

import org.fundoonotes.config.RabbitMQConfig;
import org.fundoonotes.service.EmailService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class EmailConsumer {

    private final EmailService emailService;

    public EmailConsumer(EmailService emailService) {
        this.emailService = emailService;
    }

    @RabbitListener(queues = RabbitMQConfig.QUEUE_NAME)
    public void receiveEmail(EmailMessage message) {

        System.out.println("Message Received");

        emailService.sendOtp(
                message.getToEmail(),
                message.getOtp()
        );
    }
}
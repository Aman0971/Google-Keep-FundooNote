package org.fundoonotes.messaging;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

    /**
     * Message payload sent over RabbitMQ when an OTP email needs to be delivered.
     * Kept as a plain, JSON-serializable object so Jackson2JsonMessageConverter
     * can (de)serialize it on the queue.
     */
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public class EmailMessage implements Serializable {

        private String toEmail;
        private String otp;
    }


package org.fundoonotes.service;

import org.fundoonotes.model.Note;

public interface EmailService {

    void sendOtp(String to, String otp);
    void sendCollaboratorMail(
            String to,
            String ownerName,
            String ownerEmail,
            String noteTitle
    );
    void sendReminderMail(String to, Note note);
}

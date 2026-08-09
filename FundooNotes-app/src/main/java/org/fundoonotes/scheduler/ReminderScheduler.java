package org.fundoonotes.scheduler;

import lombok.RequiredArgsConstructor;
import org.fundoonotes.model.Note;
import org.fundoonotes.repository.NoteRepository;
import org.fundoonotes.service.EmailService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ReminderScheduler {

    private final NoteRepository noteRepository;
    private final EmailService emailService;

    @Scheduled(fixedRate = 60000)
    public void sendReminderMail() {

        LocalDateTime now =
                LocalDateTime.now(ZoneId.of("Asia/Kolkata"));

        System.out.println("========================================");
        System.out.println("REMINDER SCHEDULER RUNNING");
        System.out.println("CURRENT SERVER TIME: " + now);
        System.out.println("========================================");

        List<Note> notes = noteRepository.findPendingReminders(now);

        System.out.println("PENDING REMINDERS FOUND: " + notes.size());

        for (Note note : notes) {

            try {

                System.out.println("Processing reminder...");
                System.out.println("NOTE ID: " + note.getId());
                System.out.println("NOTE TITLE: " + note.getTitle());
                System.out.println("REMINDER TIME: " + note.getReminderTime());
                System.out.println("USER EMAIL: " + note.getUser().getEmail());

                emailService.sendReminderMail(
                        note.getUser().getEmail(),
                        note
                );

                note.setReminderSent(true);
                noteRepository.save(note);

                System.out.println(
                        "REMINDER EMAIL SENT SUCCESSFULLY FOR NOTE ID: "
                                + note.getId()
                );

            } catch (Exception e) {

                System.out.println(
                        "FAILED TO SEND REMINDER FOR NOTE ID: "
                                + note.getId()
                );

                e.printStackTrace();
            }
        }
    }
}
package org.fundoonotes.scheduler;

import lombok.RequiredArgsConstructor;
import org.fundoonotes.model.Note;
import org.fundoonotes.repository.NoteRepository;
import org.fundoonotes.service.EmailService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.List;

@Component
@RequiredArgsConstructor
public class ReminderScheduler {

    private final NoteRepository noteRepository;
    private final EmailService emailService;

    @Scheduled(fixedRate = 60000) // every 1 minute
    public void sendReminderMail(){

        List<Note> notes = noteRepository.findPendingReminders(LocalDateTime.now());

        for(Note note : notes){

            emailService.sendReminderMail(
                    note.getUser().getEmail(),
                    note
            );

            note.setReminderSent(true);
            noteRepository.save(note);
        }
    }
}

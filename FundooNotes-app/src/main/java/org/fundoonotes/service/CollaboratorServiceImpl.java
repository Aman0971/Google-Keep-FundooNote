package org.fundoonotes.service;

import jakarta.transaction.Transactional;
import org.fundoonotes.dto.request.CollaboratorRequestDTO;
import org.fundoonotes.dto.response.CollaboratorResponseDTO;
import org.fundoonotes.messaging.EmailProducer;
import org.fundoonotes.model.Note;
import org.fundoonotes.model.NoteCollaborator;
import org.fundoonotes.model.User;
import org.fundoonotes.repository.NoteCollaboratorRepository;
import org.fundoonotes.repository.NoteRepository;
import org.fundoonotes.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;


import java.util.List;

@Service
public class CollaboratorServiceImpl implements CollaboratorService {

    private final NoteRepository noteRepository;
    private final UserRepository userRepository;
    private final NoteCollaboratorRepository collaboratorRepository;
//    private final EmailProducer emailProducer;
    private final EmailService emailService;

    public CollaboratorServiceImpl(
            NoteRepository noteRepository,
            UserRepository userRepository,
            NoteCollaboratorRepository collaboratorRepository,
//        EmailProducer emailProducer,
        EmailService emailService){

        this.noteRepository = noteRepository;
        this.userRepository = userRepository;
        this.collaboratorRepository = collaboratorRepository;
//        this.emailProducer = emailProducer;
        this.emailService = emailService;
    }

    @Override
    public CollaboratorResponseDTO addCollaborator(Long noteId,
                                                   CollaboratorRequestDTO dto) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String ownerEmail = authentication.getName();

        User owner = userRepository.findByEmail(ownerEmail)
                .orElseThrow(() -> new RuntimeException("Owner not found"));

        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new RuntimeException("Note not found"));

        if (!note.getUser().getId().equals(owner.getId())) {
            throw new RuntimeException("Only owner can add collaborators");
        }

        // to make sure that owner can not make himself as collaborator.
        if (owner.getEmail().equalsIgnoreCase(dto.getEmail().trim())) {
            throw new RuntimeException("Owner cannot be collaborator");
        }

        // email normalize
        String collaboratorEmail = dto.getEmail().trim().toLowerCase();
//        User collaborator = userRepository.findByEmail(dto.getEmail())
//                .orElseThrow(() -> new RuntimeException("Collaborator not found"));

        User collaborator = userRepository.findByEmail(collaboratorEmail)
                .orElseThrow(() -> new RuntimeException("Collaborator not found"));

        if (collaboratorRepository.existsByNoteAndCollaborator(note, collaborator)) {
            throw new RuntimeException("Collaborator already added");
        }

        NoteCollaborator nc =
                new NoteCollaborator(note, collaborator);

        collaboratorRepository.save(nc);

        emailService.sendCollaboratorMail(
                collaborator.getEmail(),
                owner.getFirstName(),
                owner.getEmail(),
                note.getTitle()
        );
        return new CollaboratorResponseDTO(
                note.getId(),
                collaborator.getEmail(),
                "Collaborator Added Successfully"
        );
    }

    @Transactional
    @Override
    public String removeCollaborator(Long noteId, String email) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String ownerEmail = authentication.getName();

        User owner = userRepository.findByEmail(ownerEmail)
                .orElseThrow(() -> new RuntimeException("Owner not found"));

        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new RuntimeException("Note not found"));

        if (!note.getUser().getId().equals(owner.getId())) {
            throw new RuntimeException("Only owner can remove collaborators");
        }

        User collaborator = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        NoteCollaborator noteCollaborator =
                collaboratorRepository.findByNoteAndCollaborator(note, collaborator)
                        .orElseThrow(() ->
                                new RuntimeException("Collaborator not found"));

        collaboratorRepository.delete(noteCollaborator);
        collaboratorRepository.flush();

        return "Collaborator Removed Successfully";
    }

    @Override
    public List<String> getCollaborators(Long noteId) {

        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new RuntimeException("Note not found"));

        return collaboratorRepository.findByNote(note)
                .stream()
                .map(c -> c.getCollaborator().getEmail())
                .toList();
    }
}

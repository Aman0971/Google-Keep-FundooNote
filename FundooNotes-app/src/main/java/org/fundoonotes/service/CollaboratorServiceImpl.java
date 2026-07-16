package org.fundoonotes.service;

import org.fundoonotes.dto.request.CollaboratorRequestDTO;
import org.fundoonotes.dto.response.CollaboratorResponseDTO;
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

    public CollaboratorServiceImpl(
            NoteRepository noteRepository,
            UserRepository userRepository,
            NoteCollaboratorRepository collaboratorRepository) {

        this.noteRepository = noteRepository;
        this.userRepository = userRepository;
        this.collaboratorRepository = collaboratorRepository;
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

        User collaborator = userRepository.findByEmail(dto.getEmail())
                .orElseThrow(() -> new RuntimeException("Collaborator not found"));

        if (collaboratorRepository.existsByNoteAndCollaborator(note, collaborator)) {
            throw new RuntimeException("Collaborator already added");
        }

        NoteCollaborator nc =
                new NoteCollaborator(note, collaborator);

        collaboratorRepository.save(nc);

        return new CollaboratorResponseDTO(
                note.getId(),
                collaborator.getEmail(),
                "Collaborator Added Successfully"
        );
    }

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

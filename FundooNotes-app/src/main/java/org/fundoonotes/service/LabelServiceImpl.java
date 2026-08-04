package org.fundoonotes.service;

import jakarta.transaction.Transactional;
import org.fundoonotes.dto.request.LabelRequestDTO;
import org.fundoonotes.dto.response.LabelResponseDTO;
import org.fundoonotes.dto.response.NoteResponseDTO;
import org.fundoonotes.model.Label;
import org.fundoonotes.model.Note;
import org.fundoonotes.model.User;
import org.fundoonotes.repository.LabelRepository;
import org.fundoonotes.repository.NoteRepository;
import org.fundoonotes.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LabelServiceImpl implements LabelService {

    private final LabelRepository labelRepository;
    private final UserRepository userRepository;
    private final NoteRepository noteRepository;

    public LabelServiceImpl(LabelRepository labelRepository,
                            UserRepository userRepository,
                            NoteRepository noteRepository) {

        this.labelRepository = labelRepository;
        this.userRepository = userRepository;
        this.noteRepository = noteRepository;
    }

    private User getLoggedInUser() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User Not Found"));
    }

    @Override
    public LabelResponseDTO createLabel(LabelRequestDTO dto) {

        User user = getLoggedInUser();

        Label label = new Label();

        label.setName(dto.getName());

        label.setUser(user);

        Label saved = labelRepository.save(label);

        return new LabelResponseDTO(saved.getId(), saved.getName());
    }

    @Override
    public List<LabelResponseDTO> getLabels() {

        User user = getLoggedInUser();

        return labelRepository.findByUserId(user.getId())
                .stream()
                .map(label -> new LabelResponseDTO(
                        label.getId(),
                        label.getName()))
                .toList();
    }

    @Override
    public LabelResponseDTO updateLabel(Long id, LabelRequestDTO dto) {

        User user = getLoggedInUser();

        Label label = labelRepository.findByIdAndUserId(id,user.getId())
                .orElseThrow(() -> new RuntimeException("Label Not Found"));

        label.setName(dto.getName());

        Label updated = labelRepository.save(label);

        return new LabelResponseDTO(
                updated.getId(),
                updated.getName()
        );
    }

    @Override
    @Transactional
    public String deleteLabel(Long id) {

        User user = getLoggedInUser();

        Label label = labelRepository.findByIdAndUserId(id, user.getId())
                .orElseThrow(() -> new RuntimeException("Label Not Found"));

        // Remove relation from every note
        for (Note note : label.getNotes()) {
            note.getLabels().remove(label);
        }

        // Remove reverse relation
        label.getNotes().clear();

        // Now delete label
        labelRepository.delete(label);

        return "Label Deleted Successfully";
    }

    @Override
    public String addLabelToNote(Long noteId, Long labelId) {

        User user = getLoggedInUser();

        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new RuntimeException("Note Not Found"));

        Label label = labelRepository.findByIdAndUserId(labelId,user.getId())
                .orElseThrow(() -> new RuntimeException("Label Not Found"));

//        note.getLabels().add(label);
        if (!note.getLabels().contains(label)) {
            note.getLabels().add(label);
        }

        noteRepository.save(note);

        return "Label Added Successfully";
    }

    @Override
    public String removeLabelFromNote(Long noteId, Long labelId) {

        User user = getLoggedInUser();

        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new RuntimeException("Note Not Found"));

        Label label = labelRepository.findByIdAndUserId(labelId, user.getId())
                .orElseThrow(() -> new RuntimeException("Label Not Found"));

        note.getLabels().remove(label);

        noteRepository.save(note);

        return "Label Removed Successfully";
    }
    @Override
    @Transactional
    public List<NoteResponseDTO> getNotesByLabel(Long labelId) {

        User user = getLoggedInUser();

        Label label = labelRepository.findByIdAndUserId(labelId, user.getId())
                .orElseThrow(() -> new RuntimeException("Label Not Found"));

        return label.getNotes()
                .stream()
                .map(note -> {

                    NoteResponseDTO dto = new NoteResponseDTO();

                    dto.setId(note.getId());
                    dto.setTitle(note.getTitle());
                    dto.setDescription(note.getDescription());
                    dto.setColor(note.getColor());

                    dto.setPinned(note.isPinned());
                    dto.setArchived(note.isArchived());
                    dto.setTrashed(note.isTrashed());

                    dto.setReminderTime(note.getReminderTime());

                    return dto;

                })
                .toList();
    }
}

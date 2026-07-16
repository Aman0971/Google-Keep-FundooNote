package org.fundoonotes.service;

import org.fundoonotes.dto.request.NoteRequestDTO;
import org.fundoonotes.dto.request.ReminderRequestDTO;
import org.fundoonotes.dto.response.NoteResponseDTO;
import org.fundoonotes.model.Note;
import org.fundoonotes.model.User;
import org.fundoonotes.repository.NoteRepository;
import org.fundoonotes.repository.UserRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
    public class NoteServiceImpl implements NoteService {

        private final NoteRepository noteRepository;
        private final UserRepository userRepository;

        public NoteServiceImpl(NoteRepository noteRepository,
                               UserRepository userRepository) {

            this.noteRepository = noteRepository;
            this.userRepository = userRepository;
        }

        @CacheEvict(value = "notes", key = "'allNotes'")
        @Override
        public NoteResponseDTO createNote(NoteRequestDTO dto) {

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            String email = authentication.getName();

            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Note note = new Note();

            note.setTitle(dto.getTitle());
            note.setDescription(dto.getDescription());

            note.setPinned(false);
            note.setArchived(false);
            note.setTrashed(false);

            note.setCreatedAt(LocalDateTime.now());
            note.setUpdatedAt(LocalDateTime.now());

            note.setUser(user);

            Note savedNote = noteRepository.save(note);

            return new NoteResponseDTO(
                    savedNote.getId(),
                    savedNote.getTitle(),
                    savedNote.getDescription()
            );
        }

        @Cacheable(value = "notes",key = "'allNotes'")
        @Override
        public List<NoteResponseDTO> getAllNotes() {

            System.out.println("Fetching Notes From Database");

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

            String email = authentication.getName();

            User user = userRepository.findByEmail(email)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            List<Note> notes = noteRepository.findByUserId(user.getId());

            return notes.stream()
                    .map(note -> new NoteResponseDTO(
                            note.getId(),
                            note.getTitle(),
                            note.getDescription()
                    ))
                    .toList();
        }
    @CacheEvict(value = "notes", allEntries = true)
    @Override
    public NoteResponseDTO updateNote(Long id, NoteRequestDTO dto) {

        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Note not found"));

        note.setTitle(dto.getTitle());
        note.setDescription(dto.getDescription());
        note.setUpdatedAt(LocalDateTime.now());

        Note updated = noteRepository.save(note);

        return new NoteResponseDTO(
                updated.getId(),
                updated.getTitle(),
                updated.getDescription()
        );
    }

    @CacheEvict(value = "notes", allEntries = true)
    @Override
    public void deleteNote(Long id) {

        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Note not found"));

        noteRepository.delete(note);
    }

    @CacheEvict(value = "notes", allEntries = true)
    @Override
    public String pinNote(Long id) {

        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Note not found"));

        note.setPinned(!note.isPinned());

        noteRepository.save(note);

        return "Pin Updated Successfully";
    }

    @CacheEvict(value = "notes", allEntries = true)
    @Override
    public String archiveNote(Long id){

        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Note not found"));

        note.setArchived(!note.isArchived());

        noteRepository.save(note);

        return "Archive Updated Successfully";
    }

    @CacheEvict(value = "notes", allEntries = true)
    @Override
    public String trashNote(Long id){

        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Note not found"));

        note.setTrashed(!note.isTrashed());

        noteRepository.save(note);

        return "Trash Updated Successfully";
    }

    @Cacheable(value = "search", key = "#keyword")
    @Override
    public List<NoteResponseDTO> searchNotes(String keyword) {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User Not Found"));

        List<Note> notes =
                noteRepository
                        .findByUserIdAndTitleContainingIgnoreCaseOrUserIdAndDescriptionContainingIgnoreCase(
                                user.getId(),
                                keyword,
                                user.getId(),
                                keyword
                        );

        return notes.stream()
                .map(note -> new NoteResponseDTO(
                        note.getId(),
                        note.getTitle(),
                        note.getDescription()
                ))
                .toList();
    }

    // for reminder

    @Override
    public String setReminder(Long noteId, ReminderRequestDTO dto) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new RuntimeException("Note not found"));

        note.setReminderTime(dto.getReminderTime());

        noteRepository.save(note);

        return "Reminder Added Successfully";
    }
    @Override
    public String removeReminder(Long noteId) {

        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new RuntimeException("Note not found"));

        note.setReminderTime(null);

        noteRepository.save(note);

        return "Reminder Removed Successfully";
    }
    @Override
    public List<NoteResponseDTO> getReminderNotes() {

        Authentication authentication =
                SecurityContextHolder.getContext().getAuthentication();

        String email = authentication.getName();

        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return noteRepository
                .findByUserIdAndReminderTimeIsNotNull(user.getId())
                .stream()
                .map(note -> new NoteResponseDTO(
                        note.getId(),
                        note.getTitle(),
                        note.getDescription()
                ))
                .toList();
    }

    }

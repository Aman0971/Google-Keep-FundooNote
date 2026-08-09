package org.fundoonotes.service;

import org.fundoonotes.dto.request.NoteRequestDTO;
import org.fundoonotes.dto.request.ReminderRequestDTO;
import org.fundoonotes.dto.response.LabelResponseDTO;
import org.fundoonotes.dto.response.NoteResponseDTO;
import org.fundoonotes.model.Note;
import org.fundoonotes.model.NoteCollaborator;
import org.fundoonotes.model.User;
import org.fundoonotes.repository.NoteCollaboratorRepository;
import org.fundoonotes.repository.NoteRepository;
import org.fundoonotes.repository.UserRepository;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class NoteServiceImpl implements NoteService {

    private final NoteRepository noteRepository;
    private final UserRepository userRepository;
    private final NoteCollaboratorRepository collaboratorRepository;

    public NoteServiceImpl(NoteRepository noteRepository,
                           UserRepository userRepository,
                           NoteCollaboratorRepository collaboratorRepository) {
        this.noteRepository = noteRepository;
        this.userRepository = userRepository;
        this.collaboratorRepository = collaboratorRepository;
    }

    // ⚡ Fast, null-safe DTO mapper
    private NoteResponseDTO mapToDTO(Note note, Map<Long, List<String>> colMap) {
        List<String> collaborators = colMap.getOrDefault(note.getId(), Collections.emptyList());

        List<LabelResponseDTO> labels = note.getLabels() == null ? Collections.emptyList() :
                note.getLabels().stream()
                        .filter(Objects::nonNull)
                        .map(l -> new LabelResponseDTO(l.getId(), l.getName()))
                        .toList();

        return new NoteResponseDTO(
                note.getId(),
                note.getTitle(),
                note.getDescription(),
                note.getColor(),
                note.isPinned(),
                note.isArchived(),
                note.isTrashed(),
                note.getReminderTime(),
                labels,
                collaborators
        );
    }

    // ⚡ Eliminates N+1 query problem (fetches all collaborators in 1 batch query)
    private Map<Long, List<String>> getCollaboratorMap(List<Note> notes) {
        if (notes == null || notes.isEmpty()) return Collections.emptyMap();

        List<NoteCollaborator> allCollaborators = collaboratorRepository.findAll();
        Set<Long> noteIds = notes.stream().map(Note::getId).collect(Collectors.toSet());

        Map<Long, List<String>> map = new HashMap<>();
        for (NoteCollaborator c : allCollaborators) {
            if (c != null && c.getNote() != null && noteIds.contains(c.getNote().getId()) && c.getCollaborator() != null) {
                map.computeIfAbsent(c.getNote().getId(), k -> new ArrayList<>())
                        .add(c.getCollaborator().getEmail());
            }
        }
        return map;
    }

    @CacheEvict(value = "notes", allEntries = true)
    @Override
    public NoteResponseDTO createNote(NoteRequestDTO dto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        Note note = new Note();
        note.setTitle(dto.getTitle());
        note.setDescription(dto.getDescription());
        note.setColor(dto.getColor() == null ? "#ffffff" : dto.getColor());
        note.setPinned(dto.isPinned());
        note.setArchived(dto.isArchived());
        note.setTrashed(false);
        note.setReminderSent(false);
        note.setReminderTime(dto.getReminderTime()); // 👈 Saves reminderTime
        note.setCreatedAt(LocalDateTime.now());
        note.setUpdatedAt(LocalDateTime.now());
        note.setUser(user);

        Note savedNote = noteRepository.save(note);
        return mapToDTO(savedNote, Collections.emptyMap());
    }

    @Override
    public List<NoteResponseDTO> getAllNotes() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Note> notes = noteRepository.findByUserIdOrderByCreatedAtDesc(user.getId());
        Map<Long, List<String>> colMap = getCollaboratorMap(notes);

        return notes.stream().map(n -> mapToDTO(n, colMap)).toList();
    }

    @Override
    public List<NoteResponseDTO> getReminderNotes() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Note> notes = noteRepository.findByUserIdAndReminderTimeIsNotNull(user.getId());
        Map<Long, List<String>> colMap = getCollaboratorMap(notes);

        return notes.stream().map(n -> mapToDTO(n, colMap)).toList();
    }

    @Override
    public List<NoteResponseDTO> getArchivedNotes() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Note> notes = noteRepository.findByUserIdAndArchivedTrue(user.getId());
        Map<Long, List<String>> colMap = getCollaboratorMap(notes);

        return notes.stream().map(n -> mapToDTO(n, colMap)).toList();
    }

    @Override
    public List<NoteResponseDTO> getTrashNotes() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User not found"));

        List<Note> notes = noteRepository.findByUserIdAndTrashedTrue(user.getId());
        Map<Long, List<String>> colMap = getCollaboratorMap(notes);

        return notes.stream().map(n -> mapToDTO(n, colMap)).toList();
    }

    @CacheEvict(value = "notes", allEntries = true)
    @Override
    public NoteResponseDTO updateNote(Long id, NoteRequestDTO dto) {
        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Note not found"));

        note.setTitle(dto.getTitle());
        note.setDescription(dto.getDescription());
        note.setColor(dto.getColor());
        note.setUpdatedAt(LocalDateTime.now());

        Note updated = noteRepository.save(note);
        return mapToDTO(updated, getCollaboratorMap(List.of(updated)));
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
    public String archiveNote(Long id) {
        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Note not found"));
        note.setArchived(!note.isArchived());
        noteRepository.save(note);
        return "Archive Updated Successfully";
    }

    @CacheEvict(value = "notes", allEntries = true)
    @Override
    public String trashNote(Long id) {
        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Note not found"));
        note.setTrashed(!note.isTrashed());
        noteRepository.save(note);
        return "Trash Updated Successfully";
    }

    @Override
    public List<NoteResponseDTO> searchNotes(String keyword) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = userRepository.findByEmail(authentication.getName())
                .orElseThrow(() -> new RuntimeException("User Not Found"));

        List<Note> notes = noteRepository
                .findByUserIdAndTitleContainingIgnoreCaseOrUserIdAndDescriptionContainingIgnoreCase(
                        user.getId(), keyword, user.getId(), keyword);
        Map<Long, List<String>> colMap = getCollaboratorMap(notes);

        return notes.stream().map(n -> mapToDTO(n, colMap)).toList();
    }

    @CacheEvict(value = "notes", allEntries = true)
    @Override
    public String setReminder(Long noteId, ReminderRequestDTO dto) {
        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new RuntimeException("Note not found"));
        note.setReminderTime(dto.getReminderTime());
        note.setReminderSent(false);
        noteRepository.save(note);
        return "Reminder Added Successfully";
    }

    @CacheEvict(value = "notes", allEntries = true)
    @Override
    public String removeReminder(Long noteId) {
        Note note = noteRepository.findById(noteId)
                .orElseThrow(() -> new RuntimeException("Note not found"));
        note.setReminderTime(null);
        noteRepository.save(note);
        return "Reminder Removed Successfully";
    }

    @CacheEvict(value = "notes", allEntries = true)
    @Override
    public String restoreNote(Long id) {
        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Note not found"));
        note.setTrashed(false);
        noteRepository.save(note);
        return "Note Restored Successfully";
    }

    @CacheEvict(value = "notes", allEntries = true)
    @Override
    public String deleteForever(Long id) {
        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Note not found"));
        noteRepository.delete(note);
        return "Note Deleted Permanently";
    }

    @CacheEvict(value = "notes", allEntries = true)
    @Override
    public String changeColor(Long id, String color) {
        Note note = noteRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Note not found"));
        note.setColor(color);
        noteRepository.save(note);
        return "Color Updated Successfully";
    }
}


package org.fundoonotes.service;

import org.fundoonotes.dto.request.NoteRequestDTO;
import org.fundoonotes.dto.request.ReminderRequestDTO;
import org.fundoonotes.dto.response.NoteResponseDTO;

import java.util.List;

public interface NoteService {

    NoteResponseDTO createNote(NoteRequestDTO dto);
    List<NoteResponseDTO> getAllNotes();
    NoteResponseDTO updateNote(Long id, NoteRequestDTO dto);
    void deleteNote(Long id);
    String pinNote(Long id);
    String archiveNote(Long id);
    String trashNote(Long id);
    List<NoteResponseDTO> searchNotes(String keyword);

    String setReminder(Long noteId, ReminderRequestDTO dto);
    String removeReminder(Long noteId);
    List<NoteResponseDTO> getReminderNotes();
    }

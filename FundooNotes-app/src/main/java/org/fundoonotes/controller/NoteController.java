package org.fundoonotes.controller;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.fundoonotes.dto.request.NoteRequestDTO;
import org.fundoonotes.dto.request.ReminderRequestDTO;
import org.fundoonotes.dto.response.NoteResponseDTO;
import org.fundoonotes.service.NoteService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
    @RequestMapping("/notes")
    @SecurityRequirement(name = "Bearer Authentication")
    public class NoteController {

        private final NoteService noteService;
        public NoteController(NoteService noteService) {
            this.noteService = noteService;
        }

        @PostMapping("/create")
        public ResponseEntity<NoteResponseDTO> createNote(@RequestBody NoteRequestDTO dto){
            return new ResponseEntity<>(noteService.createNote(dto), HttpStatus.CREATED
            );
        }

        @GetMapping
        public ResponseEntity<List<NoteResponseDTO>> getAllNotes() {
            return ResponseEntity.ok(noteService.getAllNotes());
        }

    @PutMapping("/{id}")
    public ResponseEntity<NoteResponseDTO> updateNote(@PathVariable Long id, @RequestBody NoteRequestDTO dto) {

        return ResponseEntity.ok(noteService.updateNote(id, dto)
        );
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteNote(@PathVariable Long id) {

        noteService.deleteNote(id);

        return ResponseEntity.ok("Note Deleted Successfully");

    }
    @PatchMapping("/pin/{id}")
    public ResponseEntity<String> pinNote(@PathVariable Long id){

        return ResponseEntity.ok(noteService.pinNote(id));

    }
    @PatchMapping("/archive/{id}")
    public ResponseEntity<String> archiveNote(@PathVariable Long id){

        return ResponseEntity.ok(noteService.archiveNote(id));

    }
    @PatchMapping("/trash/{id}")
    public ResponseEntity<String> trashNote(@PathVariable Long id){

        return ResponseEntity.ok(noteService.trashNote(id));

    }

    @GetMapping("/search")
    public ResponseEntity<List<NoteResponseDTO>> searchNotes(@RequestParam String keyword){

        return ResponseEntity.ok(noteService.searchNotes(keyword)
        );
    }

    @PatchMapping("/{id}/reminder")
    public ResponseEntity<String> setReminder(@PathVariable Long id, @RequestBody ReminderRequestDTO dto){

        return ResponseEntity.ok(noteService.setReminder(id,dto)
        );
    }

    @DeleteMapping("/{id}/reminder")
    public ResponseEntity<String> removeReminder(@PathVariable Long id){

        return ResponseEntity.ok(noteService.removeReminder(id)
        );
    }

    @GetMapping("/reminder")
    public ResponseEntity<List<NoteResponseDTO>> getReminderNotes(){

        return ResponseEntity.ok(noteService.getReminderNotes()
        );
    }
    }

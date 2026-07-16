package org.fundoonotes.controller;

import jakarta.validation.Valid;
import org.fundoonotes.dto.request.CollaboratorRequestDTO;
import org.fundoonotes.dto.response.CollaboratorResponseDTO;
import org.fundoonotes.service.CollaboratorService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/notes")
public class CollaboratorController {

    private final CollaboratorService collaboratorService;

    public CollaboratorController(CollaboratorService collaboratorService) {
        this.collaboratorService = collaboratorService;
    }

    @PostMapping("/{noteId}/collaborator")
    public ResponseEntity<CollaboratorResponseDTO> addCollaborator(
            @PathVariable Long noteId,
            @Valid @RequestBody CollaboratorRequestDTO dto) {

        return new ResponseEntity<>(
                collaboratorService.addCollaborator(noteId, dto),
                HttpStatus.OK
        );
    }

    @DeleteMapping("/{noteId}/collaborator")
    public ResponseEntity<String> removeCollaborator(
            @PathVariable Long noteId,
            @RequestParam String email) {

        return ResponseEntity.ok(
                collaboratorService.removeCollaborator(noteId, email)
        );
    }

    @GetMapping("/{noteId}/collaborator")
    public ResponseEntity<?> getCollaborators(
            @PathVariable Long noteId) {

        return ResponseEntity.ok(
                collaboratorService.getCollaborators(noteId)
        );
    }
}

package org.fundoonotes.service;

import org.fundoonotes.dto.request.CollaboratorRequestDTO;
import org.fundoonotes.dto.response.CollaboratorResponseDTO;

import java.util.List;

public interface CollaboratorService {

    CollaboratorResponseDTO addCollaborator(
            Long noteId,
            CollaboratorRequestDTO dto
    );

    String removeCollaborator(
            Long noteId,
            String email
    );

    List<String> getCollaborators(Long noteId);

}

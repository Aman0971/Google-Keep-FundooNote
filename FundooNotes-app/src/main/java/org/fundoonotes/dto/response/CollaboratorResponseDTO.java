package org.fundoonotes.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class CollaboratorResponseDTO {

    private Long noteId;

    private String collaboratorEmail;

    private String message;
}

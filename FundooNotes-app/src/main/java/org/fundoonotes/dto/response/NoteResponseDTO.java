package org.fundoonotes.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
    @Setter
    @AllArgsConstructor
@NoArgsConstructor
    public class NoteResponseDTO implements Serializable {
    private static final long serialVersionUID = 1L;

        private Long id;
        private String title;
        private String description;

    }


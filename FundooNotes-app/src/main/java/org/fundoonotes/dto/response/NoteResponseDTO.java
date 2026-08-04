package org.fundoonotes.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
    public class NoteResponseDTO implements Serializable {
    private static final long serialVersionUID = 1L;

        private Long id;
        private String title;
        private String description;
        private String color;

        private boolean pinned;
        private boolean archived;
        private boolean trashed;

        private LocalDateTime reminderTime;
        private List<LabelResponseDTO> labels;
        private List<String> collaborators;

    }


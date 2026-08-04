package org.fundoonotes.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
    @Setter
    public class NoteRequestDTO {

        private String title;
        private String description;
        private String color;

        private boolean pinned;
        private boolean archived;

        private LocalDateTime reminderTime;

        private List<Long> labelIds;

        private List<String> collaborators;

    }


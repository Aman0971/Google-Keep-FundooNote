package org.fundoonotes.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
    @Table(name = "notes")
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public class Note {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String title;

        @Column(length = 5000)
        private String description;

        private boolean pinned;

        private boolean archived;

        private boolean trashed;

        private LocalDateTime createdAt;

        private LocalDateTime updatedAt;

        @ManyToOne
        @JoinColumn(name = "user_id")
        private User user;

        @ManyToMany
        @JoinTable(
                name = "note_labels",
                joinColumns = @JoinColumn(name = "note_id"),
                inverseJoinColumns = @JoinColumn(name = "label_id")
        )
        private Set<Label> labels = new HashSet<>();

    @Column(name = "reminder_time")
    private LocalDateTime reminderTime;
    }


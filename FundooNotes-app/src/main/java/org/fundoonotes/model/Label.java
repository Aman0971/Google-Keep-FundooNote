package org.fundoonotes.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

    @Entity
    @Table(name = "labels")
    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public class Label {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        private String name;

        @ManyToOne
        @JoinColumn(name = "user_id")
        private User user;

        @ManyToMany(mappedBy = "labels")
        private Set<Note> notes = new HashSet<>();

    }

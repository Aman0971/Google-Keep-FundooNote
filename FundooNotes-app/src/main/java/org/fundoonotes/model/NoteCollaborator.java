package org.fundoonotes.model;
import jakarta.persistence.*;

    @Entity
    @Table(name = "note_collaborators")
    public class NoteCollaborator {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;

        // Note jis par collaboration hai
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "note_id", nullable = false)
        private Note note;

        // Collaborator User
        @ManyToOne(fetch = FetchType.LAZY)
        @JoinColumn(name = "user_id", nullable = false)
        private User collaborator;

        public NoteCollaborator() {
        }

        public NoteCollaborator(Note note, User collaborator) {
            this.note = note;
            this.collaborator = collaborator;
        }

        public Long getId() {
            return id;
        }

        public Note getNote() {
            return note;
        }

        public void setNote(Note note) {
            this.note = note;
        }

        public User getCollaborator() {
            return collaborator;
        }

        public void setCollaborator(User collaborator) {
            this.collaborator = collaborator;
        }
    }

package org.fundoonotes.repository;

import org.fundoonotes.model.Note;
import org.fundoonotes.model.NoteCollaborator;
import org.fundoonotes.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface NoteCollaboratorRepository
        extends JpaRepository<NoteCollaborator, Long> {

    List<NoteCollaborator> findByCollaborator(User collaborator);

    List<NoteCollaborator> findByNote(Note note);

    Optional<NoteCollaborator> findByNoteAndCollaborator(
            Note note,
            User collaborator
    );

    boolean existsByNoteAndCollaborator(
            Note note,
            User collaborator
    );


}

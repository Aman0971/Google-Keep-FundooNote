package org.fundoonotes.repository;
import org.fundoonotes.model.Note;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoteRepository extends JpaRepository<Note,Long> {
    List<Note> findByUserId(Long userId);

//    List<Note> findByUserIdAndTitleContainingIgnoreCase(Long userId, String title);
   List<Note> findByUserIdAndTitleContainingIgnoreCaseOrUserIdAndDescriptionContainingIgnoreCase(
        Long userId,
        String title,
        Long userId2,
        String description
   );

    List<Note> findByUserIdAndReminderTimeIsNotNull(Long userId);
}


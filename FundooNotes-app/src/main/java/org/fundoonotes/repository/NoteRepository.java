package org.fundoonotes.repository;
import org.fundoonotes.model.Note;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface NoteRepository extends JpaRepository<Note,Long> {
//    List<Note> findByUserId(Long userId);
List<Note> findByUserIdOrderByCreatedAtDesc(Long userId);

//    List<Note> findByUserIdAndTitleContainingIgnoreCase(Long userId, String title);
   List<Note> findByUserIdAndTitleContainingIgnoreCaseOrUserIdAndDescriptionContainingIgnoreCase(
        Long userId,
        String title,
        Long userId2,
        String description
   );

    List<Note> findByUserIdAndReminderTimeIsNotNull(Long userId);
    List<Note> findByUserIdAndArchivedTrue(Long userId);
    List<Note> findByUserIdAndTrashedTrue(Long userId);

    @Query("""
       SELECT n FROM Note n
       WHERE n.reminderTime <= :time
       AND n.reminderSent = false
       """)
    List<Note> findPendingReminders(LocalDateTime time);
}


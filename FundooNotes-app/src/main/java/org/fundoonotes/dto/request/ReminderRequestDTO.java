package org.fundoonotes.dto.request;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ReminderRequestDTO {

    private LocalDateTime reminderTime;

}

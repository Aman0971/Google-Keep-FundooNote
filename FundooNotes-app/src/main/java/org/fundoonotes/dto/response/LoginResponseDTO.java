package org.fundoonotes.dto.response;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class LoginResponseDTO {
//    private Long id;
//    private String firstName;
//    private String lastName;
//    private String email;
      private String token;
      private String message;
}

package hanu.devteria.dto.request;

import hanu.devteria.validator.DobConstraint;
import jakarta.validation.constraints.Size;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.time.LocalDate;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserCreationRequest {
    @Size(min = 3,message = "USERNAME_INVALID")
    String name;
    String email;
    @Size(min = 8,message = "PASSWORD_INVALID")
    String password;

    @DobConstraint(min = 16, message = "INVALID_DOB")
    LocalDate dob;
}

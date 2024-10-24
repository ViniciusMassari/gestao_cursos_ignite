package br.com.viniciusmassari.desafio.modules.instructor.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AuthInstructorDTO {
    @Email(message = "O campo precisa ser um email válido")
    @NotBlank(message = "O campo email precisa ser preenchido")
    private String email;

    @NotBlank(message = "O campo senha senha precisa ser preenchido")
    private String password;
}

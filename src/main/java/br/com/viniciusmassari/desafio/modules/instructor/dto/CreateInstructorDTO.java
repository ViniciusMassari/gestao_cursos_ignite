package br.com.viniciusmassari.desafio.modules.instructor.dto;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CreateInstructorDTO {
    @NotBlank(message = "O nome não pode ser vazio")
    @NotNull(message = "O campo nome não pode ser nulo")
    private String name;

    @Email(message = "O campo email precisa ser válido")
    @NotBlank(message = "O campo email não pode estar vazio")
    @NotNull(message = "O campo email não pode ser nulo")
    private String email;

    @Length(min = 6, message = "A senha precisa ter pelo menos 6 caracteres")
    @NotBlank(message = "Senha não pode ser vazia")
    @NotNull(message = "Senha precisa ser preenchida")
    private String password;
}

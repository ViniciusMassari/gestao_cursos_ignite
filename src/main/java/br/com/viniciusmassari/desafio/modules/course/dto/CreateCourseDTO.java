package br.com.viniciusmassari.desafio.modules.course.dto;

import java.util.UUID;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateCourseDTO {
    @NotBlank(message = "Nome não pode ser vazio")
    @NotNull(message = "Nome precisa ser preenchido")
    private String name;

    @NotBlank(message = "Descrição não pode ser vazia")
    @NotNull(message = "Descrição precisa ser preenchida")
    @Length(min = 10, message = "O tamanho da descrição precisa ser no mínimo de 10 caracteres")
    private String description;

    @NotBlank(message = "Categoria não pode ser vazia")
    @NotNull(message = "Categoria precisa ser preenchida")
    private String category;

    private UUID instructor_id;

}

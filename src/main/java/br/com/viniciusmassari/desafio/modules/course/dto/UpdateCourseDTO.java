package br.com.viniciusmassari.desafio.modules.course.dto;

import jakarta.validation.constraints.NotNull;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdateCourseDTO {
    @NotNull(message = "Nome não deve ser nulo")
    private String name;

    @NotNull(message = "Categoria não deve ser nula")
    private String category;
}

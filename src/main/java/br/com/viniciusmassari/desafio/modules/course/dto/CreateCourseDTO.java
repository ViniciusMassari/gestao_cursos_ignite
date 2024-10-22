package br.com.viniciusmassari.desafio.modules.course.dto;

import org.hibernate.validator.constraints.Length;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CreateCourseDTO {
    @NotBlank
    private String name;

    @NotBlank
    private String description;

    @Length(min = 1)
    @NotBlank
    @NotNull
    private String password;
}

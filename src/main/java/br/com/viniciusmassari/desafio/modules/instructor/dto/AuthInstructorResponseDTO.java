package br.com.viniciusmassari.desafio.modules.instructor.dto;

import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AuthInstructorResponseDTO {
    private String token;
}

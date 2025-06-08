package br.com.viniciusmassari.desafio.modules.instructor.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record AuthInstructorResponseDTO(
        @Schema(description = "JWT Token", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...") String token) {
}

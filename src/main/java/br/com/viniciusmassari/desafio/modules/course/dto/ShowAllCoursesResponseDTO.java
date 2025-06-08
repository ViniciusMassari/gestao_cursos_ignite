package br.com.viniciusmassari.desafio.modules.course.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import org.springframework.data.domain.Page;

public record ShowAllCoursesResponseDTO(@Schema Page<ShowCourseResponseDTO> courses) {

}

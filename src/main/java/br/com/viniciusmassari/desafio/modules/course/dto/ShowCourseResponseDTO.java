package br.com.viniciusmassari.desafio.modules.course.dto;

import java.util.UUID;

import br.com.viniciusmassari.desafio.modules.course.entity.CourseActive;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ShowCourseResponseDTO {

    private UUID courseId;
    @Schema(example = "Desenvolvimento FullStack")
    private String coursename;
    @Schema(example = "Curso de Desenvolvimento FullStack")
    private String coursedescription;
    @Schema(example = "Programação")

    private String coursecategory;

    private CourseActive courseActive;
    @Schema(example = "João Silva")
    private String instructorName;

    private UUID instructorId;
}

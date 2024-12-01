package br.com.viniciusmassari.desafio.modules.course.dto;

import java.util.UUID;

import br.com.viniciusmassari.desafio.modules.course.entity.CourseActive;
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

    private String coursename;

    private String coursedescription;

    private String coursecategory;

    private CourseActive courseActive;

    private String instructorName;
}

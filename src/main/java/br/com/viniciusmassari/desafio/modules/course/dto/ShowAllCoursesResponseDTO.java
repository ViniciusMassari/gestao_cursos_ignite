package br.com.viniciusmassari.desafio.modules.course.dto;

import java.util.List;

import br.com.viniciusmassari.desafio.modules.course.entity.CourseEntity;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShowAllCoursesResponseDTO {
    public List<CourseEntity> courses;
}

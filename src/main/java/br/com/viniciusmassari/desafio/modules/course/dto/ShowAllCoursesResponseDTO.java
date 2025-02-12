package br.com.viniciusmassari.desafio.modules.course.dto;

import br.com.viniciusmassari.desafio.modules.course.entity.CourseEntity;
import org.springframework.data.domain.Page;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ShowAllCoursesResponseDTO {
    public Page<CourseEntity> courses;
}

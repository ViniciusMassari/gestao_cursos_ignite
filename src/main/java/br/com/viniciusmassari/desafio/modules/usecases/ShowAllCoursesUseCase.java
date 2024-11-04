package br.com.viniciusmassari.desafio.modules.usecases;

import java.util.List;

import org.springframework.stereotype.Service;

import br.com.viniciusmassari.desafio.modules.course.dto.ShowAllCoursesResponseDTO;
import br.com.viniciusmassari.desafio.modules.course.entity.CourseEntity;
import br.com.viniciusmassari.desafio.modules.course.repository.CourseRepository;

@Service
public class ShowAllCoursesUseCase {
    private final CourseRepository courseRepository;

    public ShowAllCoursesUseCase(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public ShowAllCoursesResponseDTO execute() {
        List<CourseEntity> courses = this.courseRepository.findAll();
        return ShowAllCoursesResponseDTO.builder().courses(courses).build();
    }
}

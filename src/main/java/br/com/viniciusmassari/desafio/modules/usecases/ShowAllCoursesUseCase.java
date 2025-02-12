package br.com.viniciusmassari.desafio.modules.usecases;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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

    public ShowAllCoursesResponseDTO execute(int page, int perPage) {
        Pageable pageable = PageRequest.of(page, perPage);
        Page<CourseEntity> courses = this.courseRepository.findAll(pageable);
        return ShowAllCoursesResponseDTO.builder().courses(courses).build();
    }
}

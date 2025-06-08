package br.com.viniciusmassari.desafio.modules.usecases;

import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;

import br.com.viniciusmassari.desafio.modules.course.dto.*;
import br.com.viniciusmassari.desafio.modules.course.repository.CourseRepository;

@Service
public class ShowAllCoursesUseCase {
    private final CourseRepository courseRepository;

    public ShowAllCoursesUseCase(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public ShowAllCoursesResponseDTO execute(int page, int perPage) {
        Pageable pageable = PageRequest.of(page, perPage);
        Page<ShowCourseResponseDTO> courses = this.courseRepository.findAll(pageable)
                .map((courseEntity) -> new ShowCourseResponseDTO(courseEntity.getId(), courseEntity.getName(),
                        courseEntity.getDescription(), courseEntity.getCategory(), courseEntity.getActive(),
                        courseEntity.getInstructorEntity().getName(), courseEntity.getInstructorEntity().getId()));
        return new ShowAllCoursesResponseDTO(courses);
    }
}

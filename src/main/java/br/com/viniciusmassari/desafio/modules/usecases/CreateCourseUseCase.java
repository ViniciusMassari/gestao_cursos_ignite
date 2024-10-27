package br.com.viniciusmassari.desafio.modules.usecases;

import org.springframework.stereotype.Service;

import br.com.viniciusmassari.desafio.modules.course.dto.CreateCourseDTO;
import br.com.viniciusmassari.desafio.modules.course.entity.CourseActive;
import br.com.viniciusmassari.desafio.modules.course.entity.CourseEntity;
import br.com.viniciusmassari.desafio.modules.course.repository.CourseRepository;

@Service
public class CreateCourseUseCase {
    private CourseRepository courseRepository;

    public CreateCourseUseCase(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public void execute(CreateCourseDTO createCourseDTO) {
        CourseEntity courseEntity = CourseEntity.builder()
                .instructorId(createCourseDTO.getInstructor_id())
                .Active(CourseActive.ACTIVE)
                .category(createCourseDTO.getCategory())
                .description(createCourseDTO.getDescription())
                .name(createCourseDTO.getName())
                .build();

        this.courseRepository.save(courseEntity);
    }
}

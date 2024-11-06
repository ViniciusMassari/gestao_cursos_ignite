package br.com.viniciusmassari.desafio.modules.usecases;

import java.util.Optional;

import org.springframework.stereotype.Service;

import br.com.viniciusmassari.desafio.exceptions.InstructorNotFound;
import br.com.viniciusmassari.desafio.modules.course.dto.CreateCourseDTO;
import br.com.viniciusmassari.desafio.modules.course.entity.CourseActive;
import br.com.viniciusmassari.desafio.modules.course.entity.CourseEntity;
import br.com.viniciusmassari.desafio.modules.course.repository.CourseRepository;
import br.com.viniciusmassari.desafio.modules.instructor.entity.InstructorEntity;
import br.com.viniciusmassari.desafio.modules.instructor.repository.InstructorRepository;

@Service
public class CreateCourseUseCase {
    private CourseRepository courseRepository;
    private InstructorRepository instructorRepository;

    public CreateCourseUseCase(CourseRepository courseRepository, InstructorRepository instructorRepository) {
        this.courseRepository = courseRepository;
        this.instructorRepository = instructorRepository;
    }

    public void execute(CreateCourseDTO createCourseDTO) {
        Optional<InstructorEntity> instructorEntity = instructorRepository.findById(createCourseDTO.getInstructor_id());

        instructorEntity.ifPresentOrElse((foundInstructor) -> {
            CourseEntity courseEntity = CourseEntity.builder()
                    .instructorEntity(foundInstructor)
                    .Active(CourseActive.ACTIVE)
                    .category(createCourseDTO.getCategory())
                    .description(createCourseDTO.getDescription())
                    .name(createCourseDTO.getName())
                    .build();

            this.courseRepository.save(courseEntity);
        }, () -> {
            throw new InstructorNotFound();
        });

    }
}

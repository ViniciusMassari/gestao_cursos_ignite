package br.com.viniciusmassari.desafio.modules.usecases;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import br.com.viniciusmassari.desafio.exceptions.CourseNotFound;
import br.com.viniciusmassari.desafio.exceptions.NotAllowed;
import br.com.viniciusmassari.desafio.modules.course.dto.UpdateCourseDTO;
import br.com.viniciusmassari.desafio.modules.course.entity.CourseEntity;
import br.com.viniciusmassari.desafio.modules.course.repository.CourseRepository;

@Service
public class UpdateCourseUseCase {
    private CourseRepository courseRepository;

    public UpdateCourseUseCase(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public void execute(UpdateCourseDTO updateCourseDTO, UUID courseUUID, UUID instructorId) {
        Optional<CourseEntity> course = this.courseRepository.findById(courseUUID);
        course.ifPresentOrElse((foundCourse) -> {
            if (!instructorId.equals(foundCourse.getInstructorEntity().getId()))
                throw new NotAllowed();

            if (this.nameIsNotNull(updateCourseDTO.getName())) {
                foundCourse.setName(updateCourseDTO.getName());
            }

            if (this.categoryIsNotNull(updateCourseDTO.getCategory())) {
                foundCourse.setCategory(updateCourseDTO.getCategory());
            }

            this.courseRepository.save(foundCourse);
        }, () -> {
            throw new CourseNotFound();
        });
    }

    private boolean nameIsNotNull(String name) {
        return !(name == null);
    }

    private boolean categoryIsNotNull(String category) {
        return !(category == null);
    }
}

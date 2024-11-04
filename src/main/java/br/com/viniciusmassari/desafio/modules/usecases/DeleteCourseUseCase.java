package br.com.viniciusmassari.desafio.modules.usecases;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import br.com.viniciusmassari.desafio.exceptions.CourseNotFound;
import br.com.viniciusmassari.desafio.exceptions.NotAllowed;
import br.com.viniciusmassari.desafio.modules.course.entity.CourseEntity;
import br.com.viniciusmassari.desafio.modules.course.repository.CourseRepository;

@Service
public class DeleteCourseUseCase {
    private CourseRepository courseRepository;

    public DeleteCourseUseCase(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public void execute(UUID courseUUID, UUID instructorId) {
        Optional<CourseEntity> course = this.courseRepository.findById(courseUUID);
        course.ifPresentOrElse((foundCourse) -> {
            if (!instructorId.equals(foundCourse.getInstructorId()))
                throw new NotAllowed();

            this.courseRepository.deleteById(courseUUID);
        }, () -> {
            throw new CourseNotFound();
        });
    }

}

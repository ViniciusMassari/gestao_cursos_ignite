package br.com.viniciusmassari.desafio.modules.usecases;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import br.com.viniciusmassari.desafio.exceptions.CourseNotFound;
import br.com.viniciusmassari.desafio.exceptions.NotAllowed;
import br.com.viniciusmassari.desafio.modules.course.entity.CourseActive;
import br.com.viniciusmassari.desafio.modules.course.entity.CourseEntity;
import br.com.viniciusmassari.desafio.modules.course.repository.CourseRepository;

@Service
public class ChangeCourseActiveStatusUseCase {

    private CourseRepository courseRepository;

    public ChangeCourseActiveStatusUseCase(CourseRepository courseRepository) {
        this.courseRepository = courseRepository;
    }

    public void execute(UUID courseId, UUID instructorId) {
        Optional<CourseEntity> course = this.courseRepository.findById(courseId);

        course.ifPresentOrElse((foundCourse) -> {
            if (!instructorId.equals(foundCourse.getInstructorEntity().getId()))
                throw new NotAllowed();

            if (isCourseActive(foundCourse.getActive())) {
                foundCourse.setActive(CourseActive.NOT);
            } else {
                foundCourse.setActive(CourseActive.ACTIVE);
            }

            this.courseRepository.save(foundCourse);
        }, () -> {
            throw new CourseNotFound();
        });
    }

    private boolean isCourseActive(CourseActive courseActive) {
        return courseActive.equals(CourseActive.ACTIVE);
    }
}

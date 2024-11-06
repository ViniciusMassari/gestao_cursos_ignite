package br.com.viniciusmassari.desafio.usecases;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import br.com.viniciusmassari.desafio.exceptions.CourseNotFound;
import br.com.viniciusmassari.desafio.exceptions.NotAllowed;
import br.com.viniciusmassari.desafio.modules.course.entity.CourseActive;
import br.com.viniciusmassari.desafio.modules.course.entity.CourseEntity;
import br.com.viniciusmassari.desafio.modules.course.repository.CourseRepository;
import br.com.viniciusmassari.desafio.modules.instructor.entity.InstructorEntity;
import br.com.viniciusmassari.desafio.modules.usecases.ChangeCourseActiveStatusUseCase;

@RunWith(MockitoJUnitRunner.class)
public class ChangeCourseActiveStatusUseCaseTest {
    @InjectMocks
    private ChangeCourseActiveStatusUseCase changeCourseActiveStatusUseCase;

    @Mock
    private CourseRepository courseRepository;

    @DisplayName("Should not change status of a unexistent course")
    @Test
    public void should_not_change_status_of_an_unexistent_course() {
        UUID unexistentCourseUUID = UUID.randomUUID();
        UUID randomUserUUID = UUID.randomUUID();

        when(courseRepository.findById(unexistentCourseUUID)).thenReturn(Optional.empty());
        try {
            changeCourseActiveStatusUseCase.execute(unexistentCourseUUID, randomUserUUID);
        } catch (Exception e) {
            assertInstanceOf(CourseNotFound.class, e);
        }
    }

    @DisplayName("Should not change status with an unauthorized user")
    @Test
    public void should_not_change_status_with_an_unauthorized_user() {
        InstructorEntity instructorEntity = InstructorEntity.builder().id(UUID.randomUUID()).build();
        CourseEntity course = CourseEntity.builder().id(UUID.randomUUID()).instructorEntity(instructorEntity).build();
        UUID notAuthorizedUserUUID = UUID.randomUUID();

        when(courseRepository.findById(course.getId())).thenReturn(Optional.of(course));
        try {
            changeCourseActiveStatusUseCase.execute(course.getId(), notAuthorizedUserUUID);
        } catch (Exception e) {
            assertInstanceOf(NotAllowed.class, e);
        }
    }

    @DisplayName("Should change course status")
    @Test
    public void should_change_course_status() {
        InstructorEntity instructorEntity = InstructorEntity.builder().id(UUID.randomUUID()).build();
        CourseEntity course = CourseEntity.builder().id(UUID.randomUUID()).instructorEntity(instructorEntity)
                .Active(CourseActive.ACTIVE).build();

        when(courseRepository.findById(course.getId())).thenReturn(Optional.of(course));
        assertDoesNotThrow(
                () -> changeCourseActiveStatusUseCase.execute(course.getId(), course.getInstructorEntity().getId()));
    }

}

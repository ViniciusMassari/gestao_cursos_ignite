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
import br.com.viniciusmassari.desafio.modules.course.entity.CourseEntity;
import br.com.viniciusmassari.desafio.modules.course.repository.CourseRepository;
import br.com.viniciusmassari.desafio.modules.usecases.DeleteCourseUseCase;

@RunWith(MockitoJUnitRunner.class)
public class DeleteCourseUseCaseTest {

    @InjectMocks
    private DeleteCourseUseCase deleteCourseUseCase;

    @Mock
    private CourseRepository courseRepository;

    @DisplayName("Should not delete an unexistent course")
    @Test
    public void should_not_delete_an_unexistent_course() {
        UUID randomCourseUUID = UUID.randomUUID();
        UUID instructorUUID = UUID.randomUUID();
        when(courseRepository.findById(randomCourseUUID)).thenReturn(Optional.empty());
        try {
            deleteCourseUseCase.execute(randomCourseUUID, instructorUUID);
        } catch (Exception e) {
            assertInstanceOf(CourseNotFound.class, e);
        }
    }

    @DisplayName("Should not delete a course with not allowed user")
    @Test
    public void should_not_delete_a_course_with_not_allowed_user() {
        UUID notAllowdUser = UUID.randomUUID();
        CourseEntity course = CourseEntity.builder().id(UUID.randomUUID()).instructorId(UUID.randomUUID()).build();

        when(courseRepository.findById(course.getId())).thenReturn(Optional.of(course));

        try {
            deleteCourseUseCase.execute(course.getId(), notAllowdUser);
        } catch (Exception e) {
            assertInstanceOf(NotAllowed.class, e);
        }
    }

    @DisplayName("Should delete a course")
    @Test
    public void should_delete_a_course() {
        CourseEntity course = CourseEntity.builder().id(UUID.randomUUID()).instructorId(UUID.randomUUID()).build();

        when(courseRepository.findById(course.getId())).thenReturn(Optional.of(course));

        assertDoesNotThrow(() -> deleteCourseUseCase.execute(course.getId(), course.getInstructorId()));
    }
}

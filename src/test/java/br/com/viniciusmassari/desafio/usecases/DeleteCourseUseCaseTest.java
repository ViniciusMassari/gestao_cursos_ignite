package br.com.viniciusmassari.desafio.usecases;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;

import br.com.viniciusmassari.desafio.exceptions.CourseNotFound;
import br.com.viniciusmassari.desafio.exceptions.NotAllowed;
import br.com.viniciusmassari.desafio.modules.course.entity.CourseEntity;
import br.com.viniciusmassari.desafio.modules.course.repository.CourseRepository;
import br.com.viniciusmassari.desafio.modules.instructor.entity.InstructorEntity;
import br.com.viniciusmassari.desafio.modules.usecases.DeleteCourseUseCase;

@RunWith(MockitoJUnitRunner.class)
@ExtendWith(MockitoExtension.class)
class DeleteCourseUseCaseTest {

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
        InstructorEntity instructorEntity = InstructorEntity.builder().id(UUID.randomUUID()).build();
        CourseEntity course = CourseEntity.builder().id(UUID.randomUUID()).instructorEntity(instructorEntity).build();

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
        InstructorEntity instructorEntity = InstructorEntity.builder().id(UUID.randomUUID()).build();
        CourseEntity course = CourseEntity.builder().id(UUID.randomUUID()).instructorEntity(instructorEntity).build();

        when(courseRepository.findById(course.getId())).thenReturn(Optional.of(course));

        assertDoesNotThrow(() -> deleteCourseUseCase.execute(course.getId(), course.getInstructorEntity().getId()));
    }
}

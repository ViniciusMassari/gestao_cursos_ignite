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
import br.com.viniciusmassari.desafio.modules.course.dto.UpdateCourseDTO;
import br.com.viniciusmassari.desafio.modules.course.entity.CourseEntity;
import br.com.viniciusmassari.desafio.modules.course.repository.CourseRepository;
import br.com.viniciusmassari.desafio.modules.usecases.UpdateCourseUseCase;

@RunWith(MockitoJUnitRunner.class)
public class UpdateCourseUseCaseTest {

    @InjectMocks
    private UpdateCourseUseCase updateCourseUseCase;

    @Mock
    private CourseRepository courseRepository;

    @DisplayName("Should not update course if courses does not exist")
    @Test
    public void should_not_update_course_if_course_does_not_exist() {
        UpdateCourseDTO updateCourseDTO = UpdateCourseDTO.builder().category("CATEGORY").name("NAME").build();
        UUID notExistentCourseUUID = UUID.randomUUID();
        when(this.courseRepository.findById(notExistentCourseUUID)).thenReturn(Optional.empty());
        try {
            this.updateCourseUseCase.execute(updateCourseDTO, notExistentCourseUUID, null);
        } catch (Exception e) {
            assertInstanceOf(CourseNotFound.class, e);
        }
    }

    @DisplayName("Should not update course if instructor is not allowed")
    @Test
    public void should_not_update_course_if_instructor_is_not_allowed() {
        UUID instructorUUID = UUID.randomUUID();
        UUID courseUUID = UUID.randomUUID();
        UUID randomInstructorUUID = UUID.randomUUID();
        CourseEntity course = CourseEntity.builder().id(courseUUID).instructorId(instructorUUID).build();

        UpdateCourseDTO updateCourseDTO = UpdateCourseDTO.builder().category("CATEGORY").name("NAME").build();

        when(this.courseRepository.findById(courseUUID)).thenReturn(Optional.of(course));
        try {
            this.updateCourseUseCase.execute(updateCourseDTO, courseUUID, randomInstructorUUID);
        } catch (Exception e) {
            assertInstanceOf(NotAllowed.class, e);
        }
    }

    @DisplayName("Should update course")
    @Test
    public void should_update_course() {
        UUID instructorUUID = UUID.randomUUID();
        UUID courseUUID = UUID.randomUUID();
        CourseEntity course = CourseEntity.builder().id(courseUUID).instructorId(instructorUUID).build();

        UpdateCourseDTO updateCourseDTO = UpdateCourseDTO.builder().category("CATEGORY").name("NAME").build();

        when(this.courseRepository.findById(courseUUID)).thenReturn(Optional.of(course));
        when(this.courseRepository.save(course)).thenReturn(course);
        assertDoesNotThrow(() -> this.updateCourseUseCase.execute(updateCourseDTO, courseUUID, instructorUUID));

    }
}
package br.com.viniciusmassari.desafio.usecases;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Description;

import br.com.viniciusmassari.desafio.exceptions.InstructorNotFound;
import br.com.viniciusmassari.desafio.modules.course.dto.CreateCourseDTO;
import br.com.viniciusmassari.desafio.modules.course.repository.CourseRepository;
import br.com.viniciusmassari.desafio.modules.instructor.entity.InstructorEntity;
import br.com.viniciusmassari.desafio.modules.instructor.repository.InstructorRepository;
import br.com.viniciusmassari.desafio.modules.usecases.CreateCourseUseCase;

@RunWith(MockitoJUnitRunner.class)
@ExtendWith(MockitoExtension.class)
class CreateCourseUseCaseTest {

    @InjectMocks
    private CreateCourseUseCase createCourseUseCase;

    @Mock
    private CourseRepository courseRepository;

    @Mock
    private InstructorRepository instructorRepository;

    @Description("Should create a new course")
    @Test
    public void should_create_a_new_course() {
        CreateCourseDTO createCourseDTO = CreateCourseDTO.builder().category("CATEGORY").description("DESCRIPTION")
                .name("NAME").build();
        InstructorEntity instructor = InstructorEntity.builder().id(UUID.randomUUID()).build();
        createCourseDTO.setInstructor_id(instructor.getId());
        when(this.instructorRepository.findById(createCourseDTO.getInstructor_id()))
                .thenReturn(Optional.of(instructor));

        assertDoesNotThrow(() -> this.createCourseUseCase.execute(createCourseDTO));

    }

    @Description("Should not create a new course if instructor does not exist")
    @Test
    public void should_not_create_a_new_course_if_instructor_does_not_exist() {
        CreateCourseDTO createCourseDTO = CreateCourseDTO.builder().category("CATEGORY").description("DESCRIPTION")
                .name("NAME").build();
        UUID randomInstructorUUID = UUID.randomUUID();
        createCourseDTO.setInstructor_id(randomInstructorUUID);

        assertThrows(InstructorNotFound.class, () -> this.createCourseUseCase.execute(createCourseDTO));

    }

}

package br.com.viniciusmassari.desafio.usecases;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.annotation.Description;

import br.com.viniciusmassari.desafio.modules.course.dto.CreateCourseDTO;
import br.com.viniciusmassari.desafio.modules.course.repository.CourseRepository;
import br.com.viniciusmassari.desafio.modules.usecases.CreateCourseUseCase;

@RunWith(MockitoJUnitRunner.class)
public class CreateCourseUseCaseTest {

    @InjectMocks
    private CreateCourseUseCase createCourseUseCase;

    @Mock
    private CourseRepository courseRepository;

    @Description("Should create a new course")
    @Test
    public void should_create_a_new_course() {
        CreateCourseDTO createCourseDTO = CreateCourseDTO.builder().category("CATEGORY").description("DESCRIPTION")
                .name("NAME").build();

        assertDoesNotThrow(() -> this.createCourseUseCase.execute(createCourseDTO));

    }

}

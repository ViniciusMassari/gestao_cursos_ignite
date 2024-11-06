package br.com.viniciusmassari.desafio.usecases;

import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.annotation.Description;

import br.com.viniciusmassari.desafio.modules.course.entity.CourseEntity;
import br.com.viniciusmassari.desafio.modules.course.repository.CourseRepository;
import br.com.viniciusmassari.desafio.modules.usecases.ShowAllCoursesUseCase;

@RunWith(MockitoJUnitRunner.class)
@ExtendWith(MockitoExtension.class)
class ShowAllCoursesUseCaseTest {
    @InjectMocks
    private ShowAllCoursesUseCase showAllCoursesUseCase;

    @Mock
    private CourseRepository courseRepository;

    @Description("Should return an empty list of courses")
    @Test
    public void should_return_an_empty_list_of_courses() {

        assertDoesNotThrow(() -> {
            var response = this.showAllCoursesUseCase.execute();
            assertEquals(0, response.courses.size());
        });
    }

    @Description("Should return a list with one course")
    @Test
    public void should_return_an_list_with_one_course() {

        CourseEntity courseEntity = CourseEntity.builder().name("COURSE").build();
        List<CourseEntity> coursesList = new ArrayList<CourseEntity>();
        coursesList.add(courseEntity);

        when(this.courseRepository.findAll()).thenReturn(coursesList);

        assertDoesNotThrow(() -> {
            var response = this.showAllCoursesUseCase.execute();
            assertEquals(1, response.courses.size());
        });
    }

}

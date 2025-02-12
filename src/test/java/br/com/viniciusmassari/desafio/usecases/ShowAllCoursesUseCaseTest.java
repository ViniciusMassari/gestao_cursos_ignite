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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import br.com.viniciusmassari.desafio.modules.course.dto.ShowAllCoursesResponseDTO;
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
        List<CourseEntity> courses = new ArrayList<>();
        Pageable pageable = PageRequest.of(0, 10);
        Page<CourseEntity> courseEntitiesPage = new PageImpl<>(courses, pageable, 0);
        when(courseRepository.findAll(pageable)).thenReturn(courseEntitiesPage);
        assertDoesNotThrow(() -> {
            ShowAllCoursesResponseDTO response = this.showAllCoursesUseCase.execute(0, 10);
            assertEquals(0, response.courses.getContent().size());
        });
    }

    @Description("Should return a list with two courses")
    @Test
    public void should_return_an_list_with_two_courses() {

        CourseEntity courseEntity1 = CourseEntity.builder().name("COURSE").build();
        CourseEntity courseEntity2 = CourseEntity.builder().name("COURSE").build();
        List<CourseEntity> courses = new ArrayList<CourseEntity>();
        courses.add(courseEntity1);
        courses.add(courseEntity2);

        Pageable pageable = PageRequest.of(0, 10);
        Page<CourseEntity> courseEntitiesPage = new PageImpl<>(courses, pageable, 2);

        when(this.courseRepository.findAll(pageable)).thenReturn(courseEntitiesPage);

        assertDoesNotThrow(() -> {
            var response = this.showAllCoursesUseCase.execute(0, 10);
            assertEquals(2, response.courses.getContent().size());
        });
    }

}

package br.com.viniciusmassari.desafio.modules.usecases;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.viniciusmassari.desafio.exceptions.CourseNotFound;
import br.com.viniciusmassari.desafio.modules.course.dto.ShowCourseDTO;
import br.com.viniciusmassari.desafio.modules.course.dto.ShowCourseResponseDTO;
import br.com.viniciusmassari.desafio.modules.course.entity.CourseEntity;
import br.com.viniciusmassari.desafio.modules.course.repository.CourseRepository;

@Service
public class ShowCourseUseCase {
    @Autowired
    private CourseRepository courseRepository;

    public ShowCourseResponseDTO execute(ShowCourseDTO showCourseDTO) {
        Optional<CourseEntity> courseEntity = this.courseRepository.findById(showCourseDTO.getCourseId());
        var showCourseResponseDTO = ShowCourseResponseDTO.builder();
        courseEntity.ifPresentOrElse((course) -> {
            showCourseResponseDTO.courseId(course.getId()).coursename(course.getName())
                    .coursedescription(course.getDescription()).coursecategory(course.getDescription())
                    .courseActive(course.getActive()).instructorName(course.getInstructorEntity().getName());
        }, () -> {
            throw new CourseNotFound();
        });
        return showCourseResponseDTO.build();
    }
}

package br.com.viniciusmassari.desafio.modules.course.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.viniciusmassari.desafio.modules.course.dto.CreateCourseDTO;
import br.com.viniciusmassari.desafio.modules.usecases.CreateCourseUseCase;
import br.com.viniciusmassari.desafio.modules.usecases.ShowAllCoursesUseCase;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/courses")
public class CourseController {

    @Autowired
    private CreateCourseUseCase createCourseUseCase;

    @Autowired
    private ShowAllCoursesUseCase showAllCoursesUseCase;

    @PostMapping("/")
    public ResponseEntity<Object> create_course(@Valid @RequestBody CreateCourseDTO createCourseDTO,
            HttpServletRequest request) {
        try {
            UUID instructor_Id = UUID.fromString(request.getAttribute("instructor_id").toString());
            createCourseDTO.setInstructor_id(instructor_Id);

            this.createCourseUseCase.execute(createCourseDTO);

            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Não foi possível criar o curso, tenta novamente mais tarde");
        }

    }

    @GetMapping("/show/")
    public ResponseEntity<Object> show_all_courses() {
        try {
            var response = this.showAllCoursesUseCase.execute();
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body("Não foi possivel retornar os cursos, tente novamente mais tarde");
        }
    }

    @PutMapping("/{id}")
    public void update_course(@PathVariable String id) {
    }

    @PatchMapping("/{id}")
    public void change_course_active_status(@PathVariable String id) {

    }

    @DeleteMapping("/{id}")
    public void delete_course(@PathVariable String id) {

    }
}

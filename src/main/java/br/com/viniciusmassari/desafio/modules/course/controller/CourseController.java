package br.com.viniciusmassari.desafio.modules.course.controller;

import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.viniciusmassari.desafio.modules.course.dto.CreateCourseDTO;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/courses")
public class CourseController {
    @PostMapping("/")
    public ResponseEntity<Object> create_course(@Valid @RequestBody CreateCourseDTO createCourseDTO,
            HttpServletRequest request) {
        try {
            UUID instructor_Id = UUID.fromString(request.getAttribute("instructor_id").toString());
            createCourseDTO.setInstructor_id(instructor_Id);

            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Não foi possível criar o curso, tenta novamente mais tarde");
        }

    }

    @GetMapping("/show")
    public void show_courses() {
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

package br.com.viniciusmassari.desafio.modules.course.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import br.com.viniciusmassari.desafio.exceptions.CourseNotFound;
import br.com.viniciusmassari.desafio.exceptions.NotAllowed;
import br.com.viniciusmassari.desafio.modules.course.dto.CreateCourseDTO;
import br.com.viniciusmassari.desafio.modules.course.dto.UpdateCourseDTO;
import br.com.viniciusmassari.desafio.modules.usecases.ChangeCourseActiveStatusUseCase;
import br.com.viniciusmassari.desafio.modules.usecases.CreateCourseUseCase;
import br.com.viniciusmassari.desafio.modules.usecases.ShowAllCoursesUseCase;
import br.com.viniciusmassari.desafio.modules.usecases.UpdateCourseUseCase;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/courses")
public class CourseController {

    @Autowired
    private CreateCourseUseCase createCourseUseCase;

    @Autowired
    private ShowAllCoursesUseCase showAllCoursesUseCase;

    @Autowired
    private UpdateCourseUseCase updateCourseUseCase;

    @Autowired
    private ChangeCourseActiveStatusUseCase changeCourseActiveStatusUseCase;

    @PostMapping("/")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<Object> create_course(@Valid @RequestBody CreateCourseDTO createCourseDTO,
            HttpServletRequest request) {
        try {
            UUID instructor_Id = UUID.fromString(request.getAttribute("instructor_id").toString());
            createCourseDTO.setInstructor_id(instructor_Id);

            this.createCourseUseCase.execute(createCourseDTO);

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
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<Object> update_course(@PathVariable("id") String courseId,
            @Valid @RequestBody UpdateCourseDTO updateCourseDTO,
            HttpServletRequest request) {
        UUID courseUUID = UUID.fromString(courseId);
        UUID instructorUUID = UUID.fromString(request.getAttribute("instructor_id").toString());

        try {
            this.updateCourseUseCase.execute(updateCourseDTO, courseUUID, instructorUUID);
            return ResponseEntity.ok().build();
        } catch (NotAllowed e) {
            return ResponseEntity.badRequest().body("Você não está autorizado a alterar este curso");
        } catch (CourseNotFound e) {
            return ResponseEntity.badRequest().body("Curso não encontrado");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Não foi possível continuar, tente novamente mais tarde");
        }
    }

    @PatchMapping("/{id}")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<Object> change_course_active_status(@PathVariable("id") String courseid,
            HttpServletRequest request) {
        UUID instructorUUID = UUID.fromString(request.getAttribute("instructor_id").toString());
        UUID courseUUID = UUID.fromString(courseid);
        try {
            this.changeCourseActiveStatusUseCase.execute(courseUUID, instructorUUID);
            return ResponseEntity.ok().build();
        } catch (NotAllowed e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Você não pode alterar o status do curso");
        } catch (CourseNotFound e) {
            return ResponseEntity.badRequest().body("Curso não encontrado");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro interno, tente novamente mais tarde");
        }
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public void delete_course(@PathVariable String id) {

    }
}

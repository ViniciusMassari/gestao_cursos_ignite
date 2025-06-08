package br.com.viniciusmassari.desafio.modules.course.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import br.com.viniciusmassari.desafio.exceptions.CourseNotFound;
import br.com.viniciusmassari.desafio.exceptions.InstructorNotFound;
import br.com.viniciusmassari.desafio.exceptions.NotAllowed;
import br.com.viniciusmassari.desafio.modules.course.dto.CreateCourseDTO;
import br.com.viniciusmassari.desafio.modules.course.dto.ShowAllCoursesResponseDTO;
import br.com.viniciusmassari.desafio.modules.course.dto.ShowCourseDTO;
import br.com.viniciusmassari.desafio.modules.course.dto.ShowCourseResponseDTO;
import br.com.viniciusmassari.desafio.modules.course.dto.UpdateCourseDTO;
import br.com.viniciusmassari.desafio.modules.usecases.ChangeCourseActiveStatusUseCase;
import br.com.viniciusmassari.desafio.modules.usecases.CreateCourseUseCase;
import br.com.viniciusmassari.desafio.modules.usecases.DeleteCourseUseCase;
import br.com.viniciusmassari.desafio.modules.usecases.ShowAllCoursesUseCase;
import br.com.viniciusmassari.desafio.modules.usecases.ShowCourseUseCase;
import br.com.viniciusmassari.desafio.modules.usecases.UpdateCourseUseCase;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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

    @Autowired
    private DeleteCourseUseCase deleteCourseUseCase;

    @Autowired
    private ShowCourseUseCase showCourseUseCase;

    @PostMapping("/")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    @RateLimiter(name = "universalRateLimiter")
    public ResponseEntity<Object> create_course(@Valid @RequestBody CreateCourseDTO createCourseDTO,
            HttpServletRequest request) {
        try {
            UUID instructor_Id = UUID.fromString(request.getAttribute("instructor_id").toString());
            createCourseDTO.setInstructor_id(instructor_Id);

            this.createCourseUseCase.execute(createCourseDTO);

            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (InstructorNotFound e) {
            return ResponseEntity.badRequest()
                    .body("Não foi possível criar o curso, tenta novamente mais tarde");
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Não foi possível criar o curso, tenta novamente mais tarde");
        }

    }

    @GetMapping("/show/")
    @Operation(responses = { @ApiResponse(responseCode = "200", content = {
            @Content(mediaType = "application/json", schema = @Schema(implementation = ShowAllCoursesResponseDTO.class)),
            @Content(mediaType = "text/plain", schema = @Schema(example = "Não foi possivel retornar os cursos, tente novamente mais tarde")) }) })
    public ResponseEntity<Object> show_all_courses(@RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int perPage) {
        try {
            ShowAllCoursesResponseDTO response = this.showAllCoursesUseCase.execute(page, perPage);
            return ResponseEntity.ok().body(response);
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Não foi possivel retornar os cursos, tente novamente mais tarde");
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    @Operation(responses = { @ApiResponse(responseCode = "204"),
            @ApiResponse(responseCode = "400", description = "Curso não encontrado ou usuário não autorizado"),
            @ApiResponse(responseCode = "500", description = "Não foi possível continuar, tente novamente mais tarde") })
    public ResponseEntity<Object> update_course(@PathVariable("id") String courseId,
            @Valid @RequestBody UpdateCourseDTO updateCourseDTO,
            HttpServletRequest request) {
        UUID courseUUID = UUID.fromString(courseId);
        UUID instructorUUID = UUID.fromString(request.getAttribute("instructor_id").toString());

        try {
            this.updateCourseUseCase.execute(updateCourseDTO, courseUUID, instructorUUID);
            return ResponseEntity.noContent().build();
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
    @Operation(responses = {
            @ApiResponse(responseCode = "204", description = "Status do curso alterado com sucesso"),
            @ApiResponse(responseCode = "401", description = "Usuário não pode alterar o status do curso", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "400", description = "Curso não encontrado", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = @Content(mediaType = "text/plain")),
    })
    public ResponseEntity<Object> change_course_active_status(@PathVariable("id") String courseId,
            HttpServletRequest request) {
        UUID instructorUUID = UUID.fromString(request.getAttribute("instructor_id").toString());
        UUID courseUUID = UUID.fromString(courseId);
        try {
            this.changeCourseActiveStatusUseCase.execute(courseUUID, instructorUUID);
            return ResponseEntity.noContent().build();
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
    @Operation(responses = {
            @ApiResponse(responseCode = "204", description = "Status do curso alterado com sucesso"),
            @ApiResponse(responseCode = "401", description = "Usuário não pode alterar deletar o curso", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "400", description = "Curso não encontrado", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = @Content(mediaType = "text/plain")),
    })
    public ResponseEntity<Object> delete_course(@PathVariable("id") String courseId, HttpServletRequest request) {
        UUID instructorUUID = UUID.fromString(request.getAttribute("instructor_id").toString());
        UUID courseUUID = UUID.fromString(courseId);

        try {
            deleteCourseUseCase.execute(courseUUID, instructorUUID);
            return ResponseEntity.noContent().build();
        } catch (NotAllowed e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Você não está autorizado !");
        } catch (CourseNotFound e) {
            return ResponseEntity.badRequest().body("Curso não encontrado");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro interno, tente novamente mais tarde");
        }
    }

    @GetMapping("/{id}")
    @Operation(responses = {
            @ApiResponse(responseCode = "200", description = "Curso encontrado com sucesso", content = @Content(mediaType = "application/json", schema = @Schema(implementation = ShowCourseResponseDTO.class))),
            @ApiResponse(responseCode = "400", description = "Curso não encontrado", content = @Content(mediaType = "text/plain")),
            @ApiResponse(responseCode = "500", description = "Erro interno no servidor", content = @Content(mediaType = "text/plain")),
    })
    public ResponseEntity<Object> get_course(@PathVariable("id") String courseId) {
        UUID courseUUID = UUID.fromString(courseId);
        ShowCourseDTO showCourseDTO = ShowCourseDTO.builder().courseId(courseUUID).build();
        try {
            ShowCourseResponseDTO response = this.showCourseUseCase.execute(showCourseDTO);
            return ResponseEntity.ok().body(response);
        } catch (CourseNotFound e) {
            return ResponseEntity.badRequest().body("Curso não encontrado");
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Erro interno, tente novamente mais tarde");
        }
    }
}

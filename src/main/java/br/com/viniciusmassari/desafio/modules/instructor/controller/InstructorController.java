package br.com.viniciusmassari.desafio.modules.instructor.controller;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import br.com.viniciusmassari.desafio.exceptions.InstructorAlreadyExist;
import br.com.viniciusmassari.desafio.exceptions.InstructorNotFound;
import br.com.viniciusmassari.desafio.exceptions.NotAllowed;
import br.com.viniciusmassari.desafio.modules.instructor.dto.CreateInstructorDTO;
import br.com.viniciusmassari.desafio.modules.usecases.CreateInstructorUseCase;
import br.com.viniciusmassari.desafio.modules.usecases.DeleteAccountUseCase;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/instructor")
public class InstructorController {

    @Autowired
    private CreateInstructorUseCase createInstructor;

    @Autowired
    private DeleteAccountUseCase deleteAccountUseCase;

    @PostMapping("/create")
    public ResponseEntity<Object> create_account(@Valid @RequestBody CreateInstructorDTO createInstructorDTO) {

        try {
            this.createInstructor.execute(createInstructorDTO);
        } catch (InstructorAlreadyExist e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Usuário já existe");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Erro desconhecido, tente novamente mais tarde");
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    public ResponseEntity<Object> delete_account(@PathVariable String id, HttpServletRequest request) {
        UUID loggedUser = UUID.fromString(request.getAttribute("instructor_id").toString());
        UUID targetUser = UUID.fromString(id);
        try {
            deleteAccountUseCase.execute(loggedUser, targetUser);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (NotAllowed e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Você não está autorizado !");
        } catch (InstructorNotFound e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().body("Usuário não encontrado");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Erro interno, tente novamente mais tarde");
        }
    }

}

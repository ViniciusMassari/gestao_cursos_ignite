package br.com.viniciusmassari.desafio.modules.instructor.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import br.com.viniciusmassari.desafio.exceptions.InstructorAlreadyExist;
import br.com.viniciusmassari.desafio.modules.instructor.dto.CreateInstructorDTO;
import br.com.viniciusmassari.desafio.modules.usecases.CreateInstructorUseCase;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/instructor")
public class InstructorController {

    @Autowired
    private CreateInstructorUseCase createInstructor;

    @PostMapping("/")
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

    @DeleteMapping()
    public void delete_account() {
    }

}

package br.com.viniciusmassari.desafio.modules.instructor.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.viniciusmassari.desafio.exceptions.JwtCouldNotBeCreated;
import br.com.viniciusmassari.desafio.exceptions.WrongCredentials;
import br.com.viniciusmassari.desafio.modules.instructor.dto.AuthInstructorDTO;
import br.com.viniciusmassari.desafio.modules.usecases.AuthInstructorUseCase;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/instructor/auth")
public class AuthInstructorController {
    @Autowired
    AuthInstructorUseCase authInstructorUseCase;

    @PostMapping("/")
    public ResponseEntity<Object> auth_instructor(@Valid @RequestBody AuthInstructorDTO authInstructor) {
        try {
            var response = authInstructorUseCase.execute(authInstructor);
            return ResponseEntity.ok().body(response);
        } catch (JwtCouldNotBeCreated e) {
            return ResponseEntity.internalServerError()
                    .body("Um erro ocorreu ao tentar logar, tente novamente mais tarde");
        } catch (WrongCredentials e) {
            return ResponseEntity.badRequest().body("Credenciais erradas, verifique as informações e tente novamente");
        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Erro interno no servidor, tente novamente");
        }
    }
}

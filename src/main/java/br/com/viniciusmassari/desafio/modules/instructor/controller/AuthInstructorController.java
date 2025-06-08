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
import br.com.viniciusmassari.desafio.modules.instructor.dto.AuthInstructorResponseDTO;
import br.com.viniciusmassari.desafio.modules.usecases.AuthInstructorUseCase;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/instructor/auth")
@Tag(description = "Rotas referentes a autenticação do instrutor", name = "Auth instructor routes")
public class AuthInstructorController {
    @Autowired
    AuthInstructorUseCase authInstructorUseCase;

    @PostMapping("/")
    @Operation(responses = {
            @ApiResponse(responseCode = "200", content = @Content(contentSchema = @Schema(implementation = AuthInstructorResponseDTO.class))),
            @ApiResponse(responseCode = "500", content = @Content(mediaType = "text/plain", contentSchema = @Schema(example = "Um erro ocorreu ao tentar logar, tente novamente mais tarde"))),
            @ApiResponse(responseCode = "400", content = @Content(mediaType = "text/plain", contentSchema = @Schema(example = "Credenciais erradas, verifique as informações e tente novamente"))) })
    @RateLimiter(name = "universalRateLimiter")
    public ResponseEntity<?> auth_instructor(@Valid @RequestBody AuthInstructorDTO authInstructor) {
        try {
            AuthInstructorResponseDTO response = authInstructorUseCase.execute(authInstructor);
            return ResponseEntity.ok().body(response);
        } catch (JwtCouldNotBeCreated e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body("Um erro ocorreu ao tentar logar, tente novamente mais tarde");
        } catch (WrongCredentials e) {
            return ResponseEntity.badRequest().body("Credenciais erradas, verifique as informações e tente novamente");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError()
                    .body("Erro interno no servidor, tente novamente");
        }
    }
}

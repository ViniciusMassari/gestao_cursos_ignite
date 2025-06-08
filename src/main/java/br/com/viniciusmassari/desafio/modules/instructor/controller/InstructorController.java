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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/instructor")
@Tag(name = "Instructor", description = "Rotas referentes ao instrutor")
public class InstructorController {

    @Autowired
    private CreateInstructorUseCase createInstructor;

    @Autowired
    private DeleteAccountUseCase deleteAccountUseCase;

    @PostMapping("/create")
    @Operation(description = "Cria uma nova conta / Create a new account", responses = {
            @ApiResponse(responseCode = "201"), @ApiResponse(responseCode = "400", content = {
                    @Content(mediaType = "text/plain", schema = @Schema(example = "Usuário já existe")) }),
            @ApiResponse(responseCode = "500", content = {
                    @Content(mediaType = "text/plain", schema = @Schema(example = "Erro desconhecido, tente novamente mais tarde")) }) })
    public ResponseEntity<?> create_account(@Valid @RequestBody CreateInstructorDTO createInstructorDTO) {

        try {
            this.createInstructor.execute(createInstructorDTO);
        } catch (InstructorAlreadyExist e) {

            return ResponseEntity.badRequest().body("Usuário já existe");
        } catch (Exception e) {

            return ResponseEntity.internalServerError().body("Erro desconhecido, tente novamente mais tarde");
        }
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('INSTRUCTOR')")
    @Operation(description = "Deleta uma conta / Delete an account", responses = {
            @ApiResponse(responseCode = "401", content = {
                    @Content(mediaType = "text/plain", schema = @Schema(example = "Você não está autorizado !")) }),
            @ApiResponse(responseCode = "500", content = {
                    @Content(mediaType = "text/plain", schema = @Schema(example = "Erro interno, tente novamente mais tarde")) }),
            @ApiResponse(responseCode = "400", content = {
                    @Content(mediaType = "text/plain", schema = @Schema(example = "Usuário não encontrado")) }) })
    public ResponseEntity<?> delete_account(
            @Parameter(description = "Id do usuário", example = "0237d69a-0a04-4df7-9bb1-2ca83e52fae1") @PathVariable String id,
            HttpServletRequest request) {
        UUID loggedUser = UUID.fromString(request.getAttribute("instructor_id").toString());
        UUID targetUser = UUID.fromString(id);
        try {
            deleteAccountUseCase.execute(loggedUser, targetUser);
            return ResponseEntity.status(HttpStatus.OK).build();
        } catch (NotAllowed e) {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Você não está autorizado !");
        } catch (InstructorNotFound e) {

            return ResponseEntity.badRequest().body("Usuário não encontrado");
        } catch (Exception e) {

            return ResponseEntity.internalServerError().body("Erro interno, tente novamente mais tarde");
        }
    }

}

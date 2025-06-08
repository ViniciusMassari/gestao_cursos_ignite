package br.com.viniciusmassari.desafio.modules.usecases;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.viniciusmassari.desafio.exceptions.JwtCouldNotBeCreated;
import br.com.viniciusmassari.desafio.exceptions.WrongCredentials;
import br.com.viniciusmassari.desafio.modules.instructor.dto.AuthInstructorDTO;
import br.com.viniciusmassari.desafio.modules.instructor.dto.AuthInstructorResponseDTO;
import br.com.viniciusmassari.desafio.modules.instructor.entity.InstructorEntity;
import br.com.viniciusmassari.desafio.modules.instructor.repository.InstructorRepository;
import br.com.viniciusmassari.desafio.utils.TokenUtil;

@Service
public class AuthInstructorUseCase {

    private final TokenUtil tokenUtil;
    private final InstructorRepository instructorRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthInstructorUseCase(TokenUtil tokenUtil, InstructorRepository instructorRepository,
            PasswordEncoder passwordEncoder) {
        this.tokenUtil = tokenUtil;
        this.instructorRepository = instructorRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public AuthInstructorResponseDTO execute(AuthInstructorDTO authInstructor) {
        Optional<InstructorEntity> optionalInstructor = this.instructorRepository
                .findByEmail(authInstructor.getEmail());

        if (optionalInstructor.isEmpty()) {
            throw new WrongCredentials();
        }

        InstructorEntity instructor = optionalInstructor.get();
        boolean isSamePassword = this.passwordEncoder.matches(authInstructor.getPassword(), instructor.getPassword());

        if (!isSamePassword) {
            throw new WrongCredentials();
        }

        String token;
        try {
            token = tokenUtil.createToken(instructor.getId().toString());
        } catch (Exception e) {
            throw new JwtCouldNotBeCreated("It was not possible to log in, try again later", e);
        }

        return new AuthInstructorResponseDTO(token);
    }
}
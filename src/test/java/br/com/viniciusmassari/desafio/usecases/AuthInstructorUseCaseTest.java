package br.com.viniciusmassari.desafio.usecases;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import br.com.viniciusmassari.desafio.exceptions.JwtCouldNotBeCreated;
import br.com.viniciusmassari.desafio.exceptions.WrongCredentials;
import br.com.viniciusmassari.desafio.modules.instructor.dto.AuthInstructorDTO;
import br.com.viniciusmassari.desafio.modules.instructor.dto.AuthInstructorResponseDTO;
import br.com.viniciusmassari.desafio.modules.instructor.entity.InstructorEntity;
import br.com.viniciusmassari.desafio.modules.instructor.repository.InstructorRepository;
import br.com.viniciusmassari.desafio.modules.usecases.AuthInstructorUseCase;
import br.com.viniciusmassari.desafio.utils.TokenUtil;

@RunWith(MockitoJUnitRunner.class)
@ExtendWith(MockitoExtension.class)
class AuthInstructorUseCaseTest {

        @InjectMocks
        private AuthInstructorUseCase sut;

        @Mock
        private PasswordEncoder passwordEncoder;

        @Mock
        private InstructorRepository instructorRepository;

        @Mock
        private TokenUtil tokenUtil;

        @DisplayName("Should not be able to log in instructor with wrong password")
        @Test
        public void should_not_log_in_with_wrong_credencials() {
                String email = "email@example.com";
                InstructorEntity instructorEntity = InstructorEntity.builder().email(email)
                                .password("encrypted_password")
                                .build();
                AuthInstructorDTO authInstructorDTO = AuthInstructorDTO.builder().email(instructorEntity.getEmail())
                                .password("wrong_password").build();

                when(this.instructorRepository.findByEmail(authInstructorDTO.getEmail()))
                                .thenReturn(Optional.of(instructorEntity));
                when(this.passwordEncoder.matches(authInstructorDTO.getPassword(), instructorEntity.getPassword()))
                                .thenReturn(false);
                try {
                        sut.execute(authInstructorDTO);
                } catch (Exception e) {
                        assertInstanceOf(WrongCredentials.class, e);
                }
        }

        @DisplayName("Should not be able to log in if instructor does not exist")
        @Test
        public void should_not_log_in_if_instructor_does_not_exist() {
                String email = "email@example.com";

                AuthInstructorDTO authInstructorDTO = AuthInstructorDTO.builder().email(email)
                                .password("password").build();

                when(this.instructorRepository.findByEmail(authInstructorDTO.getEmail()))
                                .thenReturn(Optional.empty());

                try {
                        sut.execute(authInstructorDTO);
                } catch (Exception e) {
                        assertInstanceOf(WrongCredentials.class, e);
                }
        }

        @DisplayName("Should be able to log in if credentials are right")
        @Test
        public void should_log_in_if_credentials_are_right() throws Exception {
                String token = "TOKEN";
                String email = "email@example.com";
                UUID id = UUID.randomUUID();
                InstructorEntity instructorEntity = InstructorEntity.builder().id(id).email(email)
                                .password("encrypted_password")
                                .build();
                AuthInstructorDTO authInstructorDTO = AuthInstructorDTO.builder().email(instructorEntity.getEmail())
                                .password("password").build();

                when(this.instructorRepository.findByEmail(authInstructorDTO.getEmail()))
                                .thenReturn(Optional.of(instructorEntity));
                when(this.passwordEncoder.matches(authInstructorDTO.getPassword(), instructorEntity.getPassword()))
                                .thenReturn(true);
                when(this.tokenUtil.createToken(instructorEntity.getId().toString())).thenReturn(token);

                assertDoesNotThrow(() -> {
                        var response = sut.execute(authInstructorDTO);
                        assertInstanceOf(AuthInstructorResponseDTO.class, response);
                });
        }

        @DisplayName("Should not be able to create a token")
        @Test
        public void should_not_be_able_to_create_a_token() throws Exception {
                String email = "email@example.com";
                UUID id = UUID.randomUUID();
                InstructorEntity instructorEntity = InstructorEntity.builder().id(id).email(email)
                                .password("encrypted_password")
                                .build();
                AuthInstructorDTO authInstructorDTO = AuthInstructorDTO.builder().email(instructorEntity.getEmail())
                                .password("password").build();

                when(this.instructorRepository.findByEmail(authInstructorDTO.getEmail()))
                                .thenReturn(Optional.of(instructorEntity));
                when(this.passwordEncoder.matches(authInstructorDTO.getPassword(), instructorEntity.getPassword()))
                                .thenReturn(true);
                when(this.tokenUtil.createToken(instructorEntity.getId().toString()))
                                .thenThrow(new JwtCouldNotBeCreated("", new Exception()));

                assertThrows(JwtCouldNotBeCreated.class, () -> this.sut.execute(authInstructorDTO));
        }
}

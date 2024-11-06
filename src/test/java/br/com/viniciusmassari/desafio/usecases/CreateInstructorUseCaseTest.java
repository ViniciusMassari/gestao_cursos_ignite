package br.com.viniciusmassari.desafio.usecases;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import br.com.viniciusmassari.desafio.exceptions.InstructorAlreadyExist;
import br.com.viniciusmassari.desafio.modules.instructor.dto.CreateInstructorDTO;
import br.com.viniciusmassari.desafio.modules.instructor.entity.InstructorEntity;
import br.com.viniciusmassari.desafio.modules.instructor.repository.InstructorRepository;
import br.com.viniciusmassari.desafio.modules.usecases.CreateInstructorUseCase;

@ExtendWith(MockitoExtension.class)
class CreateInstructorUseCaseTest {

    @InjectMocks
    private CreateInstructorUseCase createInstructorUseCase;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private InstructorRepository instructorRepository;

    @DisplayName("Should return Instructor Already exist")
    @Test
    public void should_return_instructor_already_exist() {
        String email = "test@gmail.com";
        InstructorEntity instructor = InstructorEntity.builder().email(email).build();
        CreateInstructorDTO createInstructorDTO = CreateInstructorDTO.builder().email(email).build();

        when(instructorRepository.findByEmail(email)).thenReturn(Optional.of(instructor));

        try {
            createInstructorUseCase.execute(createInstructorDTO);
        } catch (Exception e) {
            assertInstanceOf(InstructorAlreadyExist.class, e);
        }

    }

    @DisplayName("Should create an Instructor ")
    @Test
    public void should_create_an_instructor() {
        String email = "test@gmail.com";
        CreateInstructorDTO createInstructorDTO = CreateInstructorDTO.builder().email(email).name("TEST_NAME")
                .password("123456").build();

        InstructorEntity instructorEntity = InstructorEntity.builder()
                .email(createInstructorDTO.getEmail())
                .name(createInstructorDTO.getName())
                .password(passwordEncoder.encode(createInstructorDTO.getPassword()))
                .build();

        when(instructorRepository.findByEmail(email)).thenReturn(Optional.empty());
        when(instructorRepository.save(instructorEntity)).thenReturn(instructorEntity);

        assertDoesNotThrow(() -> createInstructorUseCase.execute(createInstructorDTO));

    }
}

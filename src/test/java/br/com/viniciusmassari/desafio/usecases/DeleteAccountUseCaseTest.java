package br.com.viniciusmassari.desafio.usecases;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.mockito.Mockito.when;

import java.util.Optional;
import java.util.UUID;

import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import br.com.viniciusmassari.desafio.exceptions.InstructorNotFound;
import br.com.viniciusmassari.desafio.exceptions.NotAllowed;
import br.com.viniciusmassari.desafio.modules.instructor.entity.InstructorEntity;
import br.com.viniciusmassari.desafio.modules.instructor.repository.InstructorRepository;
import br.com.viniciusmassari.desafio.modules.usecases.DeleteAccountUseCase;

@RunWith(MockitoJUnitRunner.class)
public class DeleteAccountUseCaseTest {
    @InjectMocks
    private DeleteAccountUseCase deleteAccountUseCase;

    @Mock
    private InstructorRepository instructorRepository;

    @DisplayName("Should not delete an unexistent instructor")
    @Test
    public void should_not_delete_an_unexistent_instructor() {
        UUID randomInstructorUUID = UUID.randomUUID();
        UUID instructorUUID = UUID.randomUUID();
        try {
            deleteAccountUseCase.execute(randomInstructorUUID, instructorUUID);
        } catch (Exception e) {
            assertInstanceOf(InstructorNotFound.class, e);
        }
    }

    @DisplayName("Should not delete a instructor with not allowed user")
    @Test
    public void should_not_delete_a_instructor_with_not_allowed_user() {
        UUID notAllowdUser = UUID.randomUUID();
        InstructorEntity instructor = InstructorEntity.builder().id(UUID.randomUUID())
                .build();

        when(instructorRepository.findById(instructor.getId())).thenReturn(Optional.of(instructor));

        try {
            deleteAccountUseCase.execute(notAllowdUser, instructor.getId());
        } catch (Exception e) {
            assertInstanceOf(NotAllowed.class, e);
        }
    }

    @DisplayName("Should delete a instructor")
    @Test
    public void should_delete_a_instructor() {
        InstructorEntity instructor = InstructorEntity.builder().id(UUID.randomUUID()).id(UUID.randomUUID()).build();

        when(instructorRepository.findById(instructor.getId())).thenReturn(Optional.of(instructor));

        assertDoesNotThrow(() -> deleteAccountUseCase.execute(instructor.getId(), instructor.getId()));
    }
}
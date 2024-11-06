package br.com.viniciusmassari.desafio.modules.usecases;

import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Service;

import br.com.viniciusmassari.desafio.exceptions.InstructorNotFound;
import br.com.viniciusmassari.desafio.exceptions.NotAllowed;
import br.com.viniciusmassari.desafio.modules.instructor.entity.InstructorEntity;
import br.com.viniciusmassari.desafio.modules.instructor.repository.InstructorRepository;

@Service
public class DeleteAccountUseCase {
    private InstructorRepository instructorRepository;

    public DeleteAccountUseCase(InstructorRepository instructorRepository) {
        this.instructorRepository = instructorRepository;
    }

    public void execute(UUID loggedInstructorId, UUID targetInstructorId) {
        Optional<InstructorEntity> instructor = instructorRepository.findById(targetInstructorId);
        instructor.ifPresentOrElse((foundInstructor) -> {
            if (!loggedInstructorId.toString().equals(targetInstructorId.toString())) {
                throw new NotAllowed();
            }

            instructorRepository.deleteById(targetInstructorId);
        }, () -> {
            throw new InstructorNotFound();
        });
    }
}

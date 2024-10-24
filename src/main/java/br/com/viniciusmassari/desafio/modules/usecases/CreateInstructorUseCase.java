package br.com.viniciusmassari.desafio.modules.usecases;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.viniciusmassari.desafio.exceptions.InstructorAlreadyExist;
import br.com.viniciusmassari.desafio.modules.instructor.dto.CreateInstructorDTO;
import br.com.viniciusmassari.desafio.modules.instructor.entity.InstructorEntity;
import br.com.viniciusmassari.desafio.modules.instructor.repository.InstructorRepository;

@Service
public class CreateInstructorUseCase {
    @Autowired
    private InstructorRepository instructorRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void execute(CreateInstructorDTO createInstructorDTO) throws Exception {
        // var algumacoisa = this.instructorRepository.findAll();
        // for (InstructorEntity instructorEntity : algumacoisa) {
        // System.out.println(instructorEntity.getName());
        // }
        var instructor = this.instructorRepository.findByEmail(createInstructorDTO.getEmail());
        instructor.ifPresent(user -> {
            throw new InstructorAlreadyExist();
        });

        var encryptedPassword = passwordEncoder.encode(createInstructorDTO.getPassword());

        var newInstructor = InstructorEntity.builder().email(createInstructorDTO.getEmail())
                .name(createInstructorDTO.getName()).password(encryptedPassword).build();

        instructorRepository.save(newInstructor);

    }
}

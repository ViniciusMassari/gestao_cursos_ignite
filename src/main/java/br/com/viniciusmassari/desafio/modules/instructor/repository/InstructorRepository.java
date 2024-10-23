package br.com.viniciusmassari.desafio.modules.instructor.repository;

import java.util.UUID;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.viniciusmassari.desafio.modules.instructor.entity.InstructorEntity;

public interface InstructorRepository extends JpaRepository<InstructorEntity, UUID> {

    Optional<InstructorEntity> findByEmail(String email);

}

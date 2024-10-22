package br.com.viniciusmassari.desafio.modules.course.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.viniciusmassari.desafio.modules.course.entity.CourseEntity;

public interface CourseRepository extends JpaRepository<CourseEntity, UUID> {

}

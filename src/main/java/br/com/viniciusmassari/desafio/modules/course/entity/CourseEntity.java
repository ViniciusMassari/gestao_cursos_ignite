package br.com.viniciusmassari.desafio.modules.course.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import br.com.viniciusmassari.desafio.modules.instructor.entity.InstructorEntity;
import jakarta.persistence.*;
import lombok.Data;

@Entity(name = "course")
@Data
public class CourseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private String category;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CourseActive Active;

    @ManyToOne()
    @JoinColumn(name = "instructor_id", insertable = false, updatable = false)
    private InstructorEntity instructorEntity;

    @Column(name = "instructor_id", nullable = false)
    private UUID instructorId;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

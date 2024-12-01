package br.com.viniciusmassari.desafio.modules.course.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import br.com.viniciusmassari.desafio.modules.instructor.entity.InstructorEntity;
import org.hibernate.annotations.*;
import jakarta.persistence.*;
import lombok.*;

@Entity(name = "course")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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

    @ManyToOne
    @JoinColumn(name = "instructor_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private InstructorEntity instructorEntity;

    @CreationTimestamp
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}

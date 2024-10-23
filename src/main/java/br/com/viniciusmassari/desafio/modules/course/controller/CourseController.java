package br.com.viniciusmassari.desafio.modules.course.controller;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/courses")
public class CourseController {
    @PostMapping
    public void create_course() {
    }

    @GetMapping("/show")
    public void show_courses() {
    }

    @PutMapping("/{id}")
    public void update_course(@PathVariable String id) {
    }

    @PatchMapping("/{id}")
    public void change_course_active_status(@PathVariable String id) {

    }

    @DeleteMapping("/{id}")
    public void delete_course(@PathVariable String id) {

    }
}

package br.com.viniciusmassari.desafio.exceptions;

public class CourseNotFound extends UseCaseException {
    public CourseNotFound() {
        super("Course not found !");
    }

}

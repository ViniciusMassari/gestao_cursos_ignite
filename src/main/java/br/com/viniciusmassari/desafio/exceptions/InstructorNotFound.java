package br.com.viniciusmassari.desafio.exceptions;

public class InstructorNotFound extends UseCaseException {
    public InstructorNotFound() {
        super("Instructor not found");
    }
}

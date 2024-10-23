package br.com.viniciusmassari.desafio.exceptions;

public class InstructorAlreadyExist extends UseCaseException {

    public InstructorAlreadyExist() {
        super("Instructor already exists");
    }

}

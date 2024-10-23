package br.com.viniciusmassari.desafio.exceptions;

public class UseCaseException extends RuntimeException {
    public UseCaseException(String message) {
        super(message);
    }
}

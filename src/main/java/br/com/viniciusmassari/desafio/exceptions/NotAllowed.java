package br.com.viniciusmassari.desafio.exceptions;

public class NotAllowed extends UseCaseException {
    public NotAllowed() {
        super("You are not allowed to continue");
    }
}

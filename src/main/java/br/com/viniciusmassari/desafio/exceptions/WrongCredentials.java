package br.com.viniciusmassari.desafio.exceptions;

public class WrongCredentials extends UseCaseException {

    public WrongCredentials() {
        super("Wrong credentials, try again !");

    }

}

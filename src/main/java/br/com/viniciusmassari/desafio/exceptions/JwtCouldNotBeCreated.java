package br.com.viniciusmassari.desafio.exceptions;

import com.auth0.jwt.exceptions.JWTCreationException;

public class JwtCouldNotBeCreated extends JWTCreationException {

    public JwtCouldNotBeCreated(String message, Throwable cause) {
        super(message, cause);
    }

}

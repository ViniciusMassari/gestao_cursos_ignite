package br.com.viniciusmassari.desafio.utils;

import com.auth0.jwt.interfaces.DecodedJWT;

public record ValidateTokenDTO(DecodedJWT token,
        String subject) {
}

package br.com.viniciusmassari.desafio.utils;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.Duration;
import java.time.Instant;
import java.util.List;

import java.io.*;
import java.nio.charset.StandardCharsets;

import java.security.KeyFactory;
import java.util.Base64;

import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.interfaces.DecodedJWT;

@Service
public class TokenUtil {

    public String createToken(String id) throws Exception {
        List<String> roles = List.of("ROLE_INSTRUCTOR");
        try {
            Algorithm algorithm = Algorithm.RSA256(this.getPublicKey(), getPrivateKey());
            Instant expiresIn = Instant.now().plus(Duration.ofHours(2));
            return JWT.create()
                    .withIssuer("gestaocursos")
                    .withSubject(id)
                    .withClaim("roles", roles)
                    .withExpiresAt(expiresIn)
                    .sign(algorithm);
        } catch (JWTCreationException e) {
            e.printStackTrace();
            throw new JWTCreationException("JWT could not be created", e);
        }
    }

    public ValidateTokenDTO validateToken(String token) throws IllegalArgumentException, Exception {
        String newToken = token.replace("Bearer ", "");
        Algorithm algorithm = Algorithm.RSA256(getPublicKey(), null);
        JWTVerifier verifier = JWT.require(algorithm).build();
        DecodedJWT decodedJWT = verifier.verify(newToken);

        return new ValidateTokenDTO(decodedJWT, decodedJWT.getSubject());
    }

    private RSAPrivateKey getPrivateKey() throws Exception {
        return (RSAPrivateKey) getKey("private_key.pem", true);
    }

    public RSAPublicKey getPublicKey() throws Exception {
        return (RSAPublicKey) getKey("public_key.pem", false);
    }

    private Object getKey(String filename, boolean isPrivate) throws Exception {
        try (InputStream keyStream = getClass().getClassLoader().getResourceAsStream(filename)) {
            if (keyStream == null) {
                throw new FileNotFoundException("Key file not found: " + filename);
            }

            String keyContent = new String(keyStream.readAllBytes(), StandardCharsets.UTF_8);
            keyContent = keyContent.replaceAll("\\r?\\n", "")
                    .replace("-----BEGIN " + (isPrivate ? "PRIVATE" : "PUBLIC") + " KEY-----", "")
                    .replace("-----END " + (isPrivate ? "PRIVATE" : "PUBLIC") + " KEY-----", "");

            byte[] encoded = Base64.getDecoder().decode(keyContent);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");

            return isPrivate
                    ? keyFactory.generatePrivate(new PKCS8EncodedKeySpec(encoded))
                    : keyFactory.generatePublic(new X509EncodedKeySpec(encoded));
        }
    }
}
